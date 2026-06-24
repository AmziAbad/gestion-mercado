package pe.edu.cibertec.apipuestoservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apipuestoservice.dto.ActualizarTitularRequest;
import pe.edu.cibertec.apipuestoservice.dto.SocioDTO;
import pe.edu.cibertec.apipuestoservice.entity.EstadoPuesto;
import pe.edu.cibertec.apipuestoservice.entity.Puesto;
import pe.edu.cibertec.apipuestoservice.remote.client.SocioClient;
import pe.edu.cibertec.apipuestoservice.repository.PuestoRepository;
import pe.edu.cibertec.apipuestoservice.service.PuestoService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PuestoServiceImpl implements PuestoService {
    private final PuestoRepository puestoRepository;
    private final SocioClient socioClient;

    @Override
    public List<Puesto> listar() {
        return puestoRepository.findAllOrdered();
    }

    @Override
    public List<Puesto> listarOcupados() {
        return puestoRepository.findByEstadoPuesto(EstadoPuesto.OCUPADO);
    }

    @Override
    public List<Puesto> listarPorSocio(Integer idSocio) {
        return puestoRepository.findByIdSocioActual(idSocio);
    }

    @Override
    public List<Puesto> listarPorPabellon(String nombre) {
        return puestoRepository.findByPabellonIgnoreCase(nombre);
    }

    @Override
    public Optional<Puesto> obtener(Integer id) {
        return puestoRepository.findById(id);
    }

    @Override
    public Puesto crear(Puesto puesto) {
        completarDefaults(puesto);
        return puestoRepository.save(puesto);
    }

    @Override
    public Optional<Puesto> actualizar(Integer id, Puesto puesto) {
        if (!puestoRepository.existsById(id)) {
            return Optional.empty();
        }
        puesto.setIdPuesto(id);
        completarDefaults(puesto);
        return Optional.of(puestoRepository.save(puesto));
    }

    @Override
    public Optional<Puesto> actualizarTitular(Integer id, ActualizarTitularRequest request) {
        return puestoRepository.findById(id)
                .map(puesto -> {
                    Integer socioAnterior = puesto.getIdSocioActual();
                    Integer nuevoSocio = normalizarSocio(request.getIdSocioActual());

                    puesto.setIdSocioActual(nuevoSocio);

                    if (request.getEstadoPuesto() != null) {
                        puesto.setEstadoPuesto(request.getEstadoPuesto());
                    } else {
                        puesto.setEstadoPuesto(esSocioAsociacion(nuevoSocio) ? EstadoPuesto.VACANTE : EstadoPuesto.OCUPADO);
                    }
                    Puesto actualizado = puestoRepository.save(puesto);
                    verificarActividadSocio(socioAnterior);
                    verificarActividadSocio(nuevoSocio);
                    return actualizado;
                });
    }

    @Override
    public boolean eliminar(Integer id) {
        if (!puestoRepository.existsById(id)) {
            return false;
        }
        puestoRepository.deleteById(id);
        return true;
    }

    private void completarDefaults(Puesto puesto) {
        if (puesto.getMedidas() == null || puesto.getMedidas().isBlank()) {
            puesto.setMedidas("2x2m");
        }
        if (puesto.getPrecio() == null) {
            puesto.setPrecio(BigDecimal.ZERO);
        }
        
        puesto.setIdSocioActual(normalizarSocio(puesto.getIdSocioActual()));

        if (esSocioAsociacion(puesto.getIdSocioActual()) && puesto.getEstadoPuesto() == null) {
            puesto.setEstadoPuesto(EstadoPuesto.VACANTE);
        } else if (!esSocioAsociacion(puesto.getIdSocioActual())
                && (puesto.getEstadoPuesto() == null || puesto.getEstadoPuesto() == EstadoPuesto.VACANTE)) {
            puesto.setEstadoPuesto(EstadoPuesto.OCUPADO);
        }
    }

    private Integer normalizarSocio(Integer idSocio) {
        if (idSocio == null || idSocio <= 0) {
            return obtenerIdSocioAsociacion();
        }
        return idSocio;
    }

    private boolean esSocioAsociacion(Integer idSocio) {
        return idSocio != null && idSocio.equals(obtenerIdSocioAsociacion());
    }

    private void verificarActividadSocio(Integer idSocio) {
        if (idSocio == null || esSocioAsociacion(idSocio)) {
            return;
        }
        try {
            socioClient.verificarActividad(idSocio);
        } catch (Exception e) {
            System.out.println("No se pudo verificar la actividad del socio " + idSocio + ": " + e.getMessage());
        }
    }

    private Integer obtenerIdSocioAsociacion() {
        SocioDTO asociacion = socioClient.obtenerAsociacion();
        if (asociacion == null || asociacion.getIdSocio() == null) {
            throw new RuntimeException("No existe un socio marcado como Asociación.");
        }
        return asociacion.getIdSocio();
    }
}
