package pe.edu.cibertec.apitransferenciaservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.apitransferenciaservice.dto.ActualizarTitularRequest;
import pe.edu.cibertec.apitransferenciaservice.dto.PuestoDTO;

@FeignClient(name = "api-Puesto-service")
public interface PuestoClient {
    @GetMapping("/api/v1/puestos/{id}")
    PuestoDTO obtener(@PathVariable("id") Integer id);

    @PutMapping("/api/v1/puestos/{id}/titular")
    void actualizarTitular(@PathVariable("id") Integer id, @RequestBody ActualizarTitularRequest request);
}
