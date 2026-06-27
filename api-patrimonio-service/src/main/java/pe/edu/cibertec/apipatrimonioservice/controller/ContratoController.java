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
import pe.edu.cibertec.apipatrimonioservice.dto.ContratoRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.ContratoResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.FinalizarContratoRequest;
import pe.edu.cibertec.apipatrimonioservice.service.PatrimonioService;

import java.util.List;

@RestController
@RequestMapping("/contratos")
public class ContratoController {

    private final PatrimonioService patrimonioService;

    public ContratoController(PatrimonioService patrimonioService) {
        this.patrimonioService = patrimonioService;
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ContratoResponse>> listarActivos() {
        return ResponseEntity.ok(patrimonioService.listarContratosActivos());
    }

    @GetMapping("/{idContrato}")
    public ResponseEntity<ContratoResponse> buscar(@PathVariable Integer idContrato) {
        return ResponseEntity.ok(patrimonioService.buscarContrato(idContrato));
    }

    @GetMapping("/puesto/{idPuesto}/activo")
    public ResponseEntity<ContratoResponse> buscarActivoPorPuesto(@PathVariable Integer idPuesto) {
        return ResponseEntity.ok(patrimonioService.buscarContratoActivoPorPuesto(idPuesto));
    }

    @GetMapping("/socio/{idSocio}/activos")
    public ResponseEntity<List<ContratoResponse>> listarPorSocio(@PathVariable Integer idSocio) {
        return ResponseEntity.ok(patrimonioService.listarContratosActivosPorSocio(idSocio));
    }

    @PostMapping
    public ResponseEntity<ContratoResponse> aperturar(@Valid @RequestBody ContratoRequest request,
                                                      HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(patrimonioService.aperturarContrato(request, idUsuario));
    }

    @PostMapping("/{idContrato}/finalizar")
    public ResponseEntity<ContratoResponse> finalizar(@PathVariable Integer idContrato,
                                                      @Valid @RequestBody FinalizarContratoRequest request) {
        return ResponseEntity.ok(patrimonioService.finalizarContrato(idContrato, request));
    }
}
