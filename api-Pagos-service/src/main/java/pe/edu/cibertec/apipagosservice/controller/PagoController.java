package pe.edu.cibertec.apipagosservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;
import pe.edu.cibertec.apipagosservice.repository.CuotaPagoRepository;
import pe.edu.cibertec.apipagosservice.service.PagoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {
    @Autowired
    private CuotaPagoRepository pagoRepo;

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<CuotaPago> listarPagos() {
        return pagoRepo.findAll();
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
}
