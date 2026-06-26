package pe.edu.cibertec.apitesoreriaservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apitesoreriaservice.dto.ConceptoRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.ConceptoResponse;
import pe.edu.cibertec.apitesoreriaservice.service.TesoreriaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conceptos")
public class ConceptoController {

    private final TesoreriaService tesoreriaService;

    public ConceptoController(TesoreriaService tesoreriaService) {
        this.tesoreriaService = tesoreriaService;
    }

    @GetMapping
    public ResponseEntity<List<ConceptoResponse>> listar() {
        return ResponseEntity.ok(tesoreriaService.listarConceptos());
    }

    @GetMapping("/{idConcepto}")
    public ResponseEntity<ConceptoResponse> buscar(@PathVariable Integer idConcepto) {
        return ResponseEntity.ok(tesoreriaService.buscarConcepto(idConcepto));
    }

    @PostMapping
    public ResponseEntity<ConceptoResponse> registrar(@Valid @RequestBody ConceptoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tesoreriaService.registrarConcepto(request));
    }

    @PutMapping("/{idConcepto}")
    public ResponseEntity<ConceptoResponse> actualizar(@PathVariable Integer idConcepto,
                                                       @Valid @RequestBody ConceptoRequest request) {
        return ResponseEntity.ok(tesoreriaService.actualizarConcepto(idConcepto, request));
    }

    @PatchMapping("/{idConcepto}/estado")
    public ResponseEntity<ConceptoResponse> cambiarEstado(@PathVariable Integer idConcepto,
                                                          @RequestParam Boolean activo) {
        return ResponseEntity.ok(tesoreriaService.cambiarEstadoConcepto(idConcepto, activo));
    }

    @PostMapping("/{idConcepto}/activar")
    public ResponseEntity<ConceptoResponse> activar(@PathVariable Integer idConcepto) {
        return ResponseEntity.ok(tesoreriaService.cambiarEstadoConcepto(idConcepto, true));
    }

    @PostMapping("/{idConcepto}/desactivar")
    public ResponseEntity<ConceptoResponse> desactivar(@PathVariable Integer idConcepto) {
        return ResponseEntity.ok(tesoreriaService.cambiarEstadoConcepto(idConcepto, false));
    }
}
