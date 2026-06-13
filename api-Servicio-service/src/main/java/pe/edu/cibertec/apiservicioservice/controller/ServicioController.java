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
import pe.edu.cibertec.apiservicioservice.service.ServicioService;

import java.util.List;

@RestController
public class ServicioController {
    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping({"/api/v1/servicios", "/servicios"})
    public List<Servicio> listar() {
        return servicioService.listar();
    }

    @GetMapping({"/api/v1/servicios/activos", "/servicios/activos"})
    public List<Servicio> listarActivos() {
        return servicioService.listarActivos();
    }

    @GetMapping({"/api/v1/servicios/{id}", "/servicios/{id}"})
    public ResponseEntity<Servicio> obtener(@PathVariable Integer id) {
        return servicioService.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"/api/v1/servicios", "/servicios"})
    public ResponseEntity<Servicio> crear(@Valid @RequestBody Servicio servicio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.crear(servicio));
    }

    @PutMapping({"/api/v1/servicios/{id}", "/servicios/{id}"})
    public ResponseEntity<Servicio> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody Servicio servicio) {
        return servicioService.actualizar(id, servicio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"/api/v1/servicios/{id}/activar", "/servicios/{id}/activar"})
    public ResponseEntity<Servicio> activar(@PathVariable Integer id) {
        return servicioService.activar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"/api/v1/servicios/{id}/desactivar", "/servicios/{id}/desactivar"})
    public ResponseEntity<Servicio> desactivar(@PathVariable Integer id) {
        return servicioService.desactivar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/api/v1/servicios/{id}", "/servicios/{id}"})
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return servicioService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
