package pe.edu.cibertec.apiauditoriareportesservice.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apiauditoriareportesservice.dto.PadronHabilResponse;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.DeudaPendienteRemoteResponse;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.FlujoCajaRemoteResponse;
import pe.edu.cibertec.apiauditoriareportesservice.service.AuditoriaReporteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final AuditoriaReporteService auditoriaReporteService;

    public ReporteController(AuditoriaReporteService auditoriaReporteService) {
        this.auditoriaReporteService = auditoriaReporteService;
    }

    @GetMapping("/padron-habiles")
    public ResponseEntity<List<PadronHabilResponse>> padronHabiles() {
        return ResponseEntity.ok(auditoriaReporteService.padronHabiles());
    }

    @GetMapping("/morosidad")
    public ResponseEntity<List<DeudaPendienteRemoteResponse>> morosidad() {
        return ResponseEntity.ok(auditoriaReporteService.morosidad());
    }

    @GetMapping("/flujo-caja-diario")
    public ResponseEntity<List<FlujoCajaRemoteResponse>> flujoCajaDiario(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(auditoriaReporteService.flujoCajaDiario(fecha));
    }

    @GetMapping("/padron-habiles/pdf")
    public ResponseEntity<byte[]> padronHabilesPdf() {
        return pdf("padron-habiles.pdf", auditoriaReporteService.padronHabilesPdf());
    }

    @GetMapping("/morosidad/pdf")
    public ResponseEntity<byte[]> morosidadPdf() {
        return pdf("morosidad.pdf", auditoriaReporteService.morosidadPdf());
    }

    @GetMapping("/flujo-caja-diario/pdf")
    public ResponseEntity<byte[]> flujoCajaDiarioPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return pdf("flujo-caja-diario.pdf", auditoriaReporteService.flujoCajaDiarioPdf(fecha));
    }

    private ResponseEntity<byte[]> pdf(String filename, byte[] content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());
        return ResponseEntity.ok().headers(headers).body(content);
    }
}
