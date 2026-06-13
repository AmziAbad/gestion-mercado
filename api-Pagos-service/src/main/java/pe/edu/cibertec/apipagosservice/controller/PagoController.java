package pe.edu.cibertec.apipagosservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.apipagosservice.dto.DeudaDTO;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;
import pe.edu.cibertec.apipagosservice.service.PagoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

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
}
