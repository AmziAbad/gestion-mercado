package pe.edu.cibertec.apitesoreriaservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apitesoreriaservice.dto.ComprobanteResponse;
import pe.edu.cibertec.apitesoreriaservice.service.TesoreriaService;

@RestController
@RequestMapping("/api/v1/comprobantes")
public class ComprobanteController {

    private final TesoreriaService tesoreriaService;

    public ComprobanteController(TesoreriaService tesoreriaService) {
        this.tesoreriaService = tesoreriaService;
    }

    @GetMapping("/{idComprobante}")
    public ResponseEntity<ComprobanteResponse> buscar(@PathVariable Integer idComprobante) {
        return ResponseEntity.ok(tesoreriaService.buscarComprobante(idComprobante));
    }

    @GetMapping("/pago/{idPago}")
    public ResponseEntity<ComprobanteResponse> buscarPorPago(@PathVariable Integer idPago) {
        return ResponseEntity.ok(tesoreriaService.buscarComprobantePorPago(idPago));
    }
}
