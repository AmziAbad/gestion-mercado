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
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaAnulacionRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaEspecificaRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaExoneracionRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaMasivaRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaResponse;
import pe.edu.cibertec.apitesoreriaservice.service.TesoreriaService;

import java.util.List;

@RestController
@RequestMapping("/cuotas")
public class CuotaController {

    private final TesoreriaService tesoreriaService;

    public CuotaController(TesoreriaService tesoreriaService) {
        this.tesoreriaService = tesoreriaService;
    }

    @GetMapping
    public ResponseEntity<List<CuotaResponse>> listar() {
        return ResponseEntity.ok(tesoreriaService.listarCuotas());
    }

    @GetMapping("/{idCuota}")
    public ResponseEntity<CuotaResponse> buscar(@PathVariable Integer idCuota) {
        return ResponseEntity.ok(tesoreriaService.buscarCuota(idCuota));
    }

    @PostMapping("/masivas")
    public ResponseEntity<List<CuotaResponse>> generarMasivas(@Valid @RequestBody CuotaMasivaRequest request,
                                                              HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(tesoreriaService.generarCuotasMasivas(request, idUsuario));
    }

    @PostMapping("/especifica")
    public ResponseEntity<CuotaResponse> generarEspecifica(@Valid @RequestBody CuotaEspecificaRequest request,
                                                           HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(tesoreriaService.generarCuotaEspecifica(request, idUsuario));
    }

    @PatchMapping("/{idCuota}/anular")
    public ResponseEntity<CuotaResponse> anular(@PathVariable Integer idCuota,
                                                @Valid @RequestBody CuotaAnulacionRequest request,
                                                HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.ok(tesoreriaService.anularCuota(idCuota, request, idUsuario));
    }

    @PatchMapping("/{idCuota}/exonerar")
    public ResponseEntity<CuotaResponse> exonerar(@PathVariable Integer idCuota,
                                                  @Valid @RequestBody CuotaExoneracionRequest request,
                                                  HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.ok(tesoreriaService.exonerarCuota(idCuota, request, idUsuario));
    }
}
