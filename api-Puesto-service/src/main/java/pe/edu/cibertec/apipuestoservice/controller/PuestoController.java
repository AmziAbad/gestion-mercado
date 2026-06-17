package pe.edu.cibertec.apipuestoservice.controller;

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
import pe.edu.cibertec.apipuestoservice.dto.ActualizarTitularRequest;
import pe.edu.cibertec.apipuestoservice.entity.Puesto;
import pe.edu.cibertec.apipuestoservice.service.PuestoService;

import java.util.List;

@RestController
public class PuestoController {
    private final PuestoService puestoService;

    public PuestoController(PuestoService puestoService) {
        this.puestoService = puestoService;
    }

    @GetMapping({"/api/v1/puestos", "/puestos"})
    public List<Puesto> listar() {
        return puestoService.listar();
    }

    @GetMapping({"/api/v1/puestos/ocupados", "/puestos/ocupados"})
    public List<Puesto> listarOcupados() {
        return puestoService.listarOcupados();
    }

    @GetMapping({"/api/v1/puestos/socio/{idSocio}", "/puestos/socio/{idSocio}"})
    public List<Puesto> listarPorSocio(@PathVariable Integer idSocio) {
        return puestoService.listarPorSocio(idSocio);
    }

    @GetMapping({"/api/v1/puestos/pabellon/{nombre}", "/puestos/pabellon/{nombre}"})
    public List<Puesto> listarPorPabellon(@PathVariable String nombre) {
        return puestoService.listarPorPabellon(nombre);
    }

    @GetMapping({"/api/v1/puestos/{id}", "/puestos/{id}"})
    public ResponseEntity<Puesto> obtener(@PathVariable Integer id) {
        return puestoService.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"/api/v1/puestos", "/puestos"})
    public ResponseEntity<Puesto> crear(@Valid @RequestBody Puesto puesto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(puestoService.crear(puesto));
    }

    @PutMapping({"/api/v1/puestos/{id}", "/puestos/{id}"})
    public ResponseEntity<Puesto> actualizar(@PathVariable Integer id,
                                             @Valid @RequestBody Puesto puesto) {
        return puestoService.actualizar(id, puesto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping({"/api/v1/puestos/{id}/titular", "/puestos/{id}/titular"})
    public ResponseEntity<Puesto> actualizarTitular(@PathVariable Integer id,
                                                    @RequestBody ActualizarTitularRequest request) {
        return puestoService.actualizarTitular(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/api/v1/puestos/{id}", "/puestos/{id}"})
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return puestoService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
