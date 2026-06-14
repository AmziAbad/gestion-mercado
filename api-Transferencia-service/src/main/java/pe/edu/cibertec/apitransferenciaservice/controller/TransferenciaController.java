package pe.edu.cibertec.apitransferenciaservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.apitransferenciaservice.dto.TransferenciaRequest;
import pe.edu.cibertec.apitransferenciaservice.entity.Transferencia;
import pe.edu.cibertec.apitransferenciaservice.service.TransferenciaService;

import java.util.List;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @GetMapping
    public List<Transferencia> listar() {
        return transferenciaService.listar();
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody TransferenciaRequest request) {
        try {
            Transferencia nuevaTransferencia = transferenciaService.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTransferencia);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al procesar la transferencia: " + e.getMessage());
        }
    }
}
