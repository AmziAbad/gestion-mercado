package pe.edu.cibertec.apipagosservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.apipagosservice.dto.ComprobanteDTO;
import pe.edu.cibertec.apipagosservice.dto.DeudaDTO;
import pe.edu.cibertec.apipagosservice.dto.EstadoDeudoresDTO;
import pe.edu.cibertec.apipagosservice.dto.FlujoCajaDiarioDTO;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;
import pe.edu.cibertec.apipagosservice.service.PagoService;
import pe.edu.cibertec.apipagosservice.service.ReportePdfService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @Autowired
    private ReportePdfService reportePdfService;

    @GetMapping
    public List<CuotaPago> listarPagos() {
        return pagoService.listarPagos();
    }

    @PostMapping("/generar")
    public Map<String, Object> generar(@RequestParam Integer mes, @RequestParam Integer anio) {
        int total = pagoService.generarCuotasMensuales(mes, anio);
        return Map.of(
                "total", total,
                "mensaje", total > 0
                        ? "Se generaron " + total + " recibos para " + mes + "/" + anio
                        : "No se generaron recibos nuevos para " + mes + "/" + anio
        );
    }

    @PostMapping("/cuotas/{id}/pagar")
    public CuotaPago pagarCuota(@PathVariable Integer id,
                                @RequestBody(required = false) Map<String, String> pago)
    {
        return pagoService.pagarCuota(id, pago);

    }

    @GetMapping("/cuotas/puesto/{idPuesto}")
    public List<CuotaPago> listarCuotasPorPuesto(@PathVariable Integer idPuesto) {
        return pagoService.listarCuotasPorPuesto(idPuesto);
    }

    @GetMapping("/cuotas/puesto/{idPuesto}/pendientes")
    public List<CuotaPago> listarCuotasPendientesPorPuesto(@PathVariable Integer idPuesto) {
        return pagoService.listarCuotasPendientesPorPuesto(idPuesto);
    }

    @GetMapping("/deuda/puesto/{idPuesto}")
    public DeudaDTO obtenerDeudaPorPuesto(@PathVariable Integer idPuesto) {
        return pagoService.obtenerDeudaPorPuesto(idPuesto);
    }

    @GetMapping("/deuda/socio/{idSocio}")
    public DeudaDTO obtenerDeudaPorSocio(@PathVariable Integer idSocio) {
        return pagoService.obtenerDeudaPorSocio(idSocio);
    }

    @PostMapping("/deuda/puesto/{idPuesto}/generar")
    public CuotaPago generarDeudaEspecifica(@PathVariable Integer idPuesto,
                                            @RequestBody Map<String, Object> body) {
        Integer idServicio = (Integer) body.get("idServicio");
        Double monto = Double.valueOf(body.get("monto").toString());
        Integer mes = (Integer) body.get("mes");
        Integer anio = (Integer) body.get("anio");
        return pagoService.generarDeudaEspecifica(idPuesto, idServicio, monto, mes, anio);
    }

    @PutMapping("/cuotas/{id}/exonerar")
    public CuotaPago exonerarCuota(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String motivo = body.get("motivo");
        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("El motivo de exoneración es obligatorio");
        }
        return pagoService.exonerarCuota(id, motivo);
    }

    @PutMapping("/cuotas/{id}/anular")
    public CuotaPago anularCuota(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String motivo = body.get("motivo");
        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("El motivo de anulación es obligatorio");
        }
        return pagoService.anularCuota(id, motivo);
    }

    @PostMapping("/cuotas/{id}/anular-y-reemplazar")
    public CuotaPago anularYReemplazarCuota(@PathVariable Integer id,
                                            @RequestBody Map<String, Object> body) {
        String motivo = body.get("motivo") != null ? body.get("motivo").toString() : null;
        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("El motivo de anulación es obligatorio");
        }

        Integer idServicio = toInteger(body.get("idServicio"));
        Double monto = toDouble(body.get("monto"));
        Integer mes = toInteger(body.get("mes"));
        Integer anio = toInteger(body.get("anio"));

        return pagoService.anularYReemplazarCuota(id, motivo, idServicio, monto, mes, anio);
    }

    @PutMapping("/cuotas/{id}/revertir-pago")
    public CuotaPago revertirPago(@PathVariable Integer id,
                                  @RequestBody(required = false) Map<String, String> body) {
        String motivo = body != null ? body.get("motivo") : null;
        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("El motivo de anulación del pago es obligatorio");
        }
        return pagoService.revertirPago(id, motivo);
    }

    @GetMapping("/cuotas/{id}/comprobante")
    public ComprobanteDTO generarComprobante(@PathVariable Integer id) {
        return pagoService.generarComprobante(id);
    }

    @GetMapping("/reportes/flujo-caja")
    public FlujoCajaDiarioDTO obtenerFlujoCajaDiario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return pagoService.obtenerFlujoCajaDiario(fecha);
    }

    @GetMapping("/reportes/deudores")
    public EstadoDeudoresDTO obtenerEstadoDeudores() {
        return pagoService.obtenerEstadoDeudores();
    }

    @GetMapping("/reportes/flujo-caja/pdf")
    public ResponseEntity<byte[]> descargarFlujoCajaDiarioPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        FlujoCajaDiarioDTO reporte = pagoService.obtenerFlujoCajaDiario(fecha);
        byte[] pdf = reportePdfService.generarFlujoCajaDiarioPdf(reporte);
        return responderPdf(pdf, "flujo-caja-" + fecha + ".pdf");
    }

    @GetMapping("/reportes/deudores/pdf")
    public ResponseEntity<byte[]> descargarEstadoDeudoresPdf() {
        EstadoDeudoresDTO reporte = pagoService.obtenerEstadoDeudores();
        byte[] pdf = reportePdfService.generarEstadoDeudoresPdf(reporte);
        return responderPdf(pdf, "estado-deudores.pdf");
    }

    private Integer toInteger(Object value) {
        return value != null ? Integer.valueOf(value.toString()) : null;
    }

    private Double toDouble(Object value) {
        return value != null ? Double.valueOf(value.toString()) : null;
    }

    private ResponseEntity<byte[]> responderPdf(byte[] pdf, String nombreArchivo) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
