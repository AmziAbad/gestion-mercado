package pe.edu.cibertec.apitransferenciaservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import pe.edu.cibertec.apitransferenciaservice.dto.SocioDTO;

@FeignClient(name = "api-Socio-service")
public interface SocioClient {
    @GetMapping("/api/v1/socios/{id}")
    SocioDTO obtener(@PathVariable("id") Integer id);

    @PutMapping("/api/v1/socios/{id}/verificar-actividad")
    void verificarActividad(@PathVariable("id") Integer id);
}
