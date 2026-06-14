package pe.edu.cibertec.apitransferenciaservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.apitransferenciaservice.dto.ActualizarTitularRequest;

@FeignClient(name = "api-Puesto-service")
public interface PuestoClient {
    @PutMapping("/api/v1/puestos/{id}/titular")
    void actualizarTitular(@PathVariable("id") Integer id, @RequestBody ActualizarTitularRequest request);
}
