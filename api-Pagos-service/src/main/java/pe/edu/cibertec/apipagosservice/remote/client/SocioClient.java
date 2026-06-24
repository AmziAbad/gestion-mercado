package pe.edu.cibertec.apipagosservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.apipagosservice.dto.SocioDTO;

@FeignClient(name = "api-Socio-service")
public interface SocioClient {
    @GetMapping("/api/v1/socios/{id}")
    SocioDTO getSocioById(@PathVariable("id") Integer id);
}
