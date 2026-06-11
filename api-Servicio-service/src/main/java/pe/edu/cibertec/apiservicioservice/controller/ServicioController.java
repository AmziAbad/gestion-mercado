package pe.edu.cibertec.apiservicioservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apiservicioservice.entity.Servicio;
import pe.edu.cibertec.apiservicioservice.repository.ServicioRepository;

import java.util.List;

@RestController
public class ServicioController {
    private final ServicioRepository servicioRepository;

    public ServicioController(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @GetMapping({"/api/v1/servicios", "/servicios"})
    public List<Servicio> listar() {
        return servicioRepository.findAll();
    }

    @GetMapping({"/api/v1/servicios/activos", "/servicios/activos"})
    public List<Servicio> listarActivos() {
        return servicioRepository.findByActivoTrue();
    }

    @GetMapping({"/api/v1/servicios/{id}", "/servicios/{id}"})
    public ResponseEntity<Servicio> obtener(@PathVariable Integer id) {
        return servicioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"/api/v1/servicios", "/servicios"})
    public ResponseEntity<Servicio> crear(@Valid @RequestBody Servicio servicio) {
        if (servicio.getActivo() == null) {
            servicio.setActivo(true);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioRepository.save(servicio));
    }

    @PutMapping({"/api/v1/servicios/{id}", "/servicios/{id}"})
    public ResponseEntity<Servicio> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody Servicio servicio) {
        if (!servicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        servicio.setIdServicio(id);
        if (servicio.getActivo() == null) {
            servicio.setActivo(true);
        }
        return ResponseEntity.ok(servicioRepository.save(servicio));
    }

    @PostMapping({"/api/v1/servicios/{id}/activar", "/servicios/{id}/activar"})
    public ResponseEntity<Servicio> activar(@PathVariable Integer id) {
        return cambiarEstado(id, true);
    }

    @PostMapping({"/api/v1/servicios/{id}/desactivar", "/servicios/{id}/desactivar"})
    public ResponseEntity<Servicio> desactivar(@PathVariable Integer id) {
        return cambiarEstado(id, false);
    }

    @DeleteMapping({"/api/v1/servicios/{id}", "/servicios/{id}"})
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!servicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        servicioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Servicio> cambiarEstado(Integer id, boolean activo) {
        return servicioRepository.findById(id)
                .map(servicio -> {
                    servicio.setActivo(activo);
                    return ResponseEntity.ok(servicioRepository.save(servicio));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
