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
import pe.edu.cibertec.apipatrimonioservice.dto.SocioRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.SocioResponse;
import pe.edu.cibertec.apipatrimonioservice.service.PatrimonioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/socios")
public class SocioController {

    private final PatrimonioService patrimonioService;

    public SocioController(PatrimonioService patrimonioService) {
        this.patrimonioService = patrimonioService;
    }

    @GetMapping
    public ResponseEntity<List<SocioResponse>> listar() {
        return ResponseEntity.ok(patrimonioService.listarSocios());
    }

    @GetMapping("/activos-con-contrato")
    public ResponseEntity<List<SocioResponse>> listarActivosConContrato() {
        return ResponseEntity.ok(patrimonioService.listarSociosActivosConContrato());
    }

    @GetMapping("/{idSocio}")
    public ResponseEntity<SocioResponse> buscar(@PathVariable Integer idSocio) {
        return ResponseEntity.ok(patrimonioService.buscarSocio(idSocio));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<SocioResponse> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(patrimonioService.buscarSocioPorDni(dni));
    }

    @PostMapping
    public ResponseEntity<SocioResponse> registrar(@Valid @RequestBody SocioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patrimonioService.registrarSocio(request));
    }

    @PutMapping("/{idSocio}")
    public ResponseEntity<SocioResponse> actualizar(@PathVariable Integer idSocio,
                                                    @Valid @RequestBody SocioRequest request) {
        return ResponseEntity.ok(patrimonioService.actualizarSocio(idSocio, request));
    }
}
