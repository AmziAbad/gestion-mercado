package pe.edu.cibertec.apiservicioservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apiservicioservice.entity.Servicio;
import pe.edu.cibertec.apiservicioservice.repository.ServicioRepository;
import pe.edu.cibertec.apiservicioservice.service.ServicioService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {
    private final ServicioRepository servicioRepository;

    @Override
    public List<Servicio> listar() {
        return servicioRepository.findAll();
    }

    @Override
    public List<Servicio> listarActivos() {
        return servicioRepository.findByActivoTrue();
    }

    @Override
    public Optional<Servicio> obtener(Integer id) {
        return servicioRepository.findById(id);
    }

    @Override
    public Servicio crear(Servicio servicio) {
        completarDefaults(servicio);
        return servicioRepository.save(servicio);
    }

    @Override
    public Optional<Servicio> actualizar(Integer id, Servicio servicio) {
        if (!servicioRepository.existsById(id)) {
            return Optional.empty();
        }
        servicio.setIdServicio(id);
        completarDefaults(servicio);
        return Optional.of(servicioRepository.save(servicio));
    }

    @Override
    public Optional<Servicio> activar(Integer id) {
        return cambiarEstado(id, true);
    }

    @Override
    public Optional<Servicio> desactivar(Integer id) {
        return cambiarEstado(id, false);
    }

    @Override
    public boolean eliminar(Integer id) {
        if (!servicioRepository.existsById(id)) {
            return false;
        }
        servicioRepository.deleteById(id);
        return true;
    }

    private Optional<Servicio> cambiarEstado(Integer id, boolean activo) {
        return servicioRepository.findById(id)
                .map(servicio -> {
                    servicio.setActivo(activo);
                    return servicioRepository.save(servicio);
                });
    }

    private void completarDefaults(Servicio servicio) {
        if (servicio.getActivo() == null) {
            servicio.setActivo(true);
        }
    }
}
