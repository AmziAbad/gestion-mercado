package pe.edu.cibertec.apitransferenciaservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.apitransferenciaservice.dto.DeudaDTO;

@FeignClient(name = "api-Pagos-service")
public interface PagoClient {
    @GetMapping("/api/v1/pagos/deuda/puesto/{idPuesto}")
    DeudaDTO obtenerDeudaPorPuesto(@PathVariable("idPuesto") Integer idPuesto);
}
