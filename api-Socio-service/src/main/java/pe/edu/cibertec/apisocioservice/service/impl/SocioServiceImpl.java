package pe.edu.cibertec.apisocioservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apisocioservice.dto.DeudaDTO;
import pe.edu.cibertec.apisocioservice.dto.SocioResumenDTO;
import pe.edu.cibertec.apisocioservice.entity.Socio;
import pe.edu.cibertec.apisocioservice.remote.client.PagoClient;
import pe.edu.cibertec.apisocioservice.remote.client.PuestoClient;
import pe.edu.cibertec.apisocioservice.repository.SocioRepository;
import pe.edu.cibertec.apisocioservice.service.SocioService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocioServiceImpl implements SocioService {
    private final SocioRepository socioRepository;
    private final PuestoClient puestoClient;
    private final PagoClient pagoClient;

    @Override
    public List<Socio> listarTodos() {
        return socioRepository.findAll();
    }

    @Override
    public Optional<Socio> obtenerPorId(Integer id) {
        return socioRepository.findById(id);
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return socioRepository.findByDni(dni);
    }

    @Override
    public Socio guardar(Socio socio) {
        return socioRepository.save(socio);
    }

    @Override
    public Optional<Socio> actualizar(Integer id, Socio socio) {
        if (!socioRepository.existsById(id)) {
            return Optional.empty();
        }
        socio.setIdSocio(id);
        return Optional.of(socioRepository.save(socio));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (!socioRepository.existsById(id)) {
            return false;
        }
        socioRepository.deleteById(id);
        return true;
    }

    @Override
    public List<SocioResumenDTO> obtenerResumenSocios() {
        return socioRepository.findAll().stream()
                .map(this::crearResumen)
                .toList();
    }

    private SocioResumenDTO crearResumen(Socio socio) {
        long cantidadPuestos = puestoClient.listarPorSocio(socio.getIdSocio()).size();
        DeudaDTO deuda = pagoClient.obtenerDeudaPorSocio(socio.getIdSocio());
        double totalDeuda = deuda != null && deuda.getTotalDeuda() != null
                ? deuda.getTotalDeuda()
                : 0.0;

        return SocioResumenDTO.builder()
                .idSocio(socio.getIdSocio())
                .dni(socio.getDni())
                .nombreCompleto(socio.getNombre() + " " + socio.getApellido())
                .cantidadPuestos(cantidadPuestos)
                .totalDeuda(totalDeuda)
                .estado(totalDeuda > 0 ? "EN DEUDA" : "AL DIA")
                .build();
    }
}
