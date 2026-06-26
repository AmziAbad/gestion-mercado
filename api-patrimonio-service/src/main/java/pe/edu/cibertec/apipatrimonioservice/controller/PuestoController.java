package pe.edu.cibertec.apipatrimonioservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apipatrimonioservice.dto.PuestoRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.PuestoResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.SaneamientoResponse;
import pe.edu.cibertec.apipatrimonioservice.service.PatrimonioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/puestos")
public class PuestoController {

    private final PatrimonioService patrimonioService;

    public PuestoController(PatrimonioService patrimonioService) {
        this.patrimonioService = patrimonioService;
    }

    @GetMapping
    public ResponseEntity<List<PuestoResponse>> listar() {
        return ResponseEntity.ok(patrimonioService.listarPuestos());
    }

    @GetMapping("/{idPuesto}")
    public ResponseEntity<PuestoResponse> buscar(@PathVariable Integer idPuesto) {
        return ResponseEntity.ok(patrimonioService.buscarPuesto(idPuesto));
    }

    @PostMapping
    public ResponseEntity<PuestoResponse> registrar(@Valid @RequestBody PuestoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patrimonioService.registrarPuesto(request));
    }

    @PutMapping("/{idPuesto}")
    public ResponseEntity<PuestoResponse> actualizar(@PathVariable Integer idPuesto,
                                                     @Valid @RequestBody PuestoRequest request) {
        return ResponseEntity.ok(patrimonioService.actualizarPuesto(idPuesto, request));
    }

    @GetMapping("/{idPuesto}/saneamiento")
    public ResponseEntity<SaneamientoResponse> auditarSaneamiento(@PathVariable Integer idPuesto) {
        return ResponseEntity.ok(patrimonioService.auditarSaneamiento(idPuesto));
    }
}
