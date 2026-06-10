package pe.edu.cibertec.apisocioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.apisocioservice.entity.Socio;
import pe.edu.cibertec.apisocioservice.repository.SocioRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/socios")
public class SocioController {
    @Autowired
    private SocioRepository socioRepo;

    @GetMapping
    public List<Socio> listarTodos() {
        return socioRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Socio> obtenerPorId(@PathVariable Integer id) {
        return socioRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Socio> guardar(@RequestBody Socio socio) {
        return ResponseEntity.ok(socioRepo.save(socio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizar(@PathVariable Integer id, @RequestBody Socio socio) {
        if (!socioRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        socio.setIdSocio(id);
        return ResponseEntity.ok(socioRepo.save(socio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!socioRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        socioRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
