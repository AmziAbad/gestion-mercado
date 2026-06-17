package pe.edu.cibertec.apipuestoservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apipuestoservice.dto.ActualizarTitularRequest;
import pe.edu.cibertec.apipuestoservice.entity.EstadoPuesto;
import pe.edu.cibertec.apipuestoservice.entity.Puesto;
import pe.edu.cibertec.apipuestoservice.repository.PuestoRepository;
import pe.edu.cibertec.apipuestoservice.service.PuestoService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PuestoServiceImpl implements PuestoService {
    private final PuestoRepository puestoRepository;

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
                    puesto.setIdSocioActual(request.getIdSocioActual());
                    if (request.getEstadoPuesto() != null) {
                        puesto.setEstadoPuesto(request.getEstadoPuesto());
                    } else {
                        puesto.setEstadoPuesto(request.getIdSocioActual() == null
                                ? EstadoPuesto.VACANTE
                                : EstadoPuesto.OCUPADO);
                    }
                    return puestoRepository.save(puesto);
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
        if (puesto.getEstadoPuesto() == null) {
            puesto.setEstadoPuesto(EstadoPuesto.VACANTE);
        }
    }
}
