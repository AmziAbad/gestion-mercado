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
import pe.edu.cibertec.apipuestoservice.entity.EstadoPuesto;
import pe.edu.cibertec.apipuestoservice.entity.Puesto;
import pe.edu.cibertec.apipuestoservice.repository.PuestoRepository;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class PuestoController {
    private final PuestoRepository puestoRepository;

    public PuestoController(PuestoRepository puestoRepository) {
        this.puestoRepository = puestoRepository;
    }

    @GetMapping({"/api/v1/puestos", "/puestos"})
    public List<Puesto> listar() {
        return puestoRepository.findAllOrdered();
    }

    @GetMapping({"/api/v1/puestos/ocupados", "/puestos/ocupados"})
    public List<Puesto> listarOcupados() {
        return puestoRepository.findByEstadoPuesto(EstadoPuesto.OCUPADO);
    }

    @GetMapping({"/api/v1/puestos/socio/{idSocio}", "/puestos/socio/{idSocio}"})
    public List<Puesto> listarPorSocio(@PathVariable Integer idSocio) {
        return puestoRepository.findByIdSocioActual(idSocio);
    }

    @GetMapping({"/api/v1/puestos/pabellon/{nombre}", "/puestos/pabellon/{nombre}"})
    public List<Puesto> listarPorPabellon(@PathVariable String nombre) {
        return puestoRepository.findByPabellonIgnoreCase(nombre);
    }

    @GetMapping({"/api/v1/puestos/{id}", "/puestos/{id}"})
    public ResponseEntity<Puesto> obtener(@PathVariable Integer id) {
        return puestoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"/api/v1/puestos", "/puestos"})
    public ResponseEntity<Puesto> crear(@Valid @RequestBody Puesto puesto) {
        completarDefaults(puesto);
        return ResponseEntity.status(HttpStatus.CREATED).body(puestoRepository.save(puesto));
    }

    @PutMapping({"/api/v1/puestos/{id}", "/puestos/{id}"})
    public ResponseEntity<Puesto> actualizar(@PathVariable Integer id,
                                             @Valid @RequestBody Puesto puesto) {
        if (!puestoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        puesto.setIdPuesto(id);
        completarDefaults(puesto);
        return ResponseEntity.ok(puestoRepository.save(puesto));
    }

    @DeleteMapping({"/api/v1/puestos/{id}", "/puestos/{id}"})
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!puestoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        puestoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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
