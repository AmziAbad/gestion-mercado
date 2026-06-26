package pe.edu.cibertec.apitesoreriaservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apitesoreriaservice.config.UsuarioHeader;
import pe.edu.cibertec.apitesoreriaservice.dto.TurnoAperturaRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.TurnoCierreRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.TurnoResponse;
import pe.edu.cibertec.apitesoreriaservice.service.TesoreriaService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
public class TurnoController {

    private final TesoreriaService tesoreriaService;

    public TurnoController(TesoreriaService tesoreriaService) {
        this.tesoreriaService = tesoreriaService;
    }

    @GetMapping
    public ResponseEntity<List<TurnoResponse>> listarPorFecha(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(tesoreriaService.listarTurnosPorFecha(fecha));
    }

    @PostMapping("/aperturar")
    public ResponseEntity<TurnoResponse> aperturar(@Valid @RequestBody TurnoAperturaRequest request,
                                                   HttpServletRequest servletRequest) {
        Integer idUsuario = UsuarioHeader.obtenerIdUsuario(servletRequest);
        return ResponseEntity.ok(tesoreriaService.aperturarTurno(request, idUsuario));
    }

    @PostMapping("/{idTurno}/cerrar")
    public ResponseEntity<TurnoResponse> cerrar(@PathVariable Integer idTurno,
                                                @Valid @RequestBody TurnoCierreRequest request) {
        return ResponseEntity.ok(tesoreriaService.cerrarTurno(idTurno, request));
    }
}
