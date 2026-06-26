package pe.edu.cibertec.apitesoreriaservice.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apitesoreriaservice.dto.DeudaPendienteReporteResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.FlujoCajaReporteResponse;
import pe.edu.cibertec.apitesoreriaservice.service.TesoreriaService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteDatosController {

    private final TesoreriaService tesoreriaService;

    public ReporteDatosController(TesoreriaService tesoreriaService) {
        this.tesoreriaService = tesoreriaService;
    }

    @GetMapping("/morosidad/datos")
    public ResponseEntity<List<DeudaPendienteReporteResponse>> morosidad() {
        return ResponseEntity.ok(tesoreriaService.deudasPendientes());
    }

    @GetMapping("/flujo-caja-diario/datos")
    public ResponseEntity<List<FlujoCajaReporteResponse>> flujoCaja(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(tesoreriaService.flujoCajaDiario(fecha));
    }
}
