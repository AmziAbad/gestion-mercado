package pe.edu.cibertec.apitesoreriaservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apitesoreriaservice.config.UsuarioHeader;
import pe.edu.cibertec.apitesoreriaservice.dto.PagoExtornoRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.PagoRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.PagoResponse;
import pe.edu.cibertec.apitesoreriaservice.service.TesoreriaService;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final TesoreriaService tesoreriaService;

    public PagoController(TesoreriaService tesoreriaService) {
        this.tesoreriaService = tesoreriaService;
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<PagoResponse> buscar(@PathVariable Integer idPago) {
        return ResponseEntity.ok(tesoreriaService.buscarPago(idPago));
    }

    @PostMapping
    public ResponseEntity<PagoResponse> registrar(@Valid @RequestBody PagoRequest request,
                                                  HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(tesoreriaService.registrarPago(request, idUsuario));
    }

    @PatchMapping("/{idPago}/extornar")
    public ResponseEntity<PagoResponse> extornar(@PathVariable Integer idPago,
                                                 @Valid @RequestBody PagoExtornoRequest request,
                                                 HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.ok(tesoreriaService.extornarPago(idPago, request, idUsuario));
    }
}
