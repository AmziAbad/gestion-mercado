package pe.edu.cibertec.apisocioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.apisocioservice.dto.SocioResumenDTO;
import pe.edu.cibertec.apisocioservice.entity.Socio;
import pe.edu.cibertec.apisocioservice.service.SocioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/socios")
public class SocioController {
    @Autowired
    private SocioService socioService;

    @GetMapping
    public List<Socio> listarTodos() {
        return socioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Socio> obtenerPorId(@PathVariable Integer id) {
        return socioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/resumen")
    public List<SocioResumenDTO> listarResumen() {
        return socioService.obtenerResumenSocios();
    }

    @GetMapping("/buscar/{dni}")
    public ResponseEntity<Socio> buscarPorDni(@PathVariable String dni) {
        return socioService.buscarPorDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Socio> guardar(@RequestBody Socio socio) {
        return ResponseEntity.ok(socioService.guardar(socio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizar(@PathVariable Integer id, @RequestBody Socio socio) {
        return socioService.actualizar(id, socio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return socioService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
