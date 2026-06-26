package pe.edu.cibertec.apipatrimonioservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apipatrimonioservice.config.UsuarioHeader;
import pe.edu.cibertec.apipatrimonioservice.dto.TransferenciaRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.TransferenciaResponse;
import pe.edu.cibertec.apipatrimonioservice.service.PatrimonioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transferencias")
public class TransferenciaController {

    private final PatrimonioService patrimonioService;

    public TransferenciaController(PatrimonioService patrimonioService) {
        this.patrimonioService = patrimonioService;
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaResponse>> listar() {
        return ResponseEntity.ok(patrimonioService.listarTransferencias());
    }

    @GetMapping("/{idTransferencia}")
    public ResponseEntity<TransferenciaResponse> buscar(@PathVariable Integer idTransferencia) {
        return ResponseEntity.ok(patrimonioService.buscarTransferencia(idTransferencia));
    }

    @PostMapping
    public ResponseEntity<TransferenciaResponse> ejecutar(@Valid @RequestBody TransferenciaRequest request,
                                                          HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(patrimonioService.ejecutarTransferencia(request, idUsuario));
    }
}
