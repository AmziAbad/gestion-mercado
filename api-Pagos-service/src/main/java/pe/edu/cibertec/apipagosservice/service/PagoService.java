package pe.edu.cibertec.apipagosservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apipagosservice.dto.PuestoDTO;
import pe.edu.cibertec.apipagosservice.dto.ServicioDTO;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;
import pe.edu.cibertec.apipagosservice.entity.EstadoPago;
import pe.edu.cibertec.apipagosservice.entity.MetodoPago;
import pe.edu.cibertec.apipagosservice.repository.CuotaPagoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PagoService {
    private final ServicioClient  servicioClient;
    private final CuotaPagoRepository cuotaRepo;
    private final PuestoClient puestoClient;

    @Transactional
    public int generarCuotasMensuales(int mes, int anio) {
        int cont = 0;

        List<ServicioDTO> servicios = servicioClient.getServiciosActivos();
        List<PuestoDTO> puestosOcupados = puestoClient.getPuestosOcupados();

        if (puestosOcupados.isEmpty()) return 0;

        for (ServicioDTO serv : servicios) {
            double montoCuota;
            if ("FIJO".equals(serv.getTipoCobro())) {
                montoCuota = serv.getMontoFijoPuesto();
            } else {
                montoCuota = serv.getCostoTotalExterno() / puestosOcupados.size();
            }

            for (PuestoDTO puesto : puestosOcupados) {
                boolean existe = cuotaRepo.existsByIdPuestoAndIdServicioAndMesAndAnio(
                        puesto.getIdPuesto(), serv.getIdServicio(), mes, anio
                );

                if (!existe) {
                    CuotaPago cuota = CuotaPago.builder()
                            .idPuesto(puesto.getIdPuesto())
                            .idServicio(serv.getIdServicio())
                            .monto(montoCuota)
                            .mes(mes)
                            .anio(anio)
                            .estado(EstadoPago.PENDIENTE)
                            .build();
                    cuotaRepo.save(cuota);
                    cont++;
                }
            }
        }
        return cont;
    }

    public CuotaPago pagarCuota(Integer id, Map<String, String> pago) {
        return cuotaRepo.findById(id).map(c -> {
            c.setEstado(EstadoPago.PAGADO);
            c.setFechaPago(LocalDateTime.now());
            if (pago != null) {
                String metodo = pago.get("metodoPago");
                if (metodo != null && !metodo.isBlank()) {
                    c.setMetodoPago(MetodoPago.valueOf(metodo));
                }
                c.setNumeroOperacion(pago.get("numeroOperacion"));
            }
            return cuotaRepo.save(c);
        }).orElseThrow(() -> new RuntimeException("Cuota no encontrada"));
    }

}
