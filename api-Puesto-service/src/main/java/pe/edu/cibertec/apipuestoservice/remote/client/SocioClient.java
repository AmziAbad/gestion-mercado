package pe.edu.cibertec.apipuestoservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import pe.edu.cibertec.apipuestoservice.dto.SocioDTO;

@FeignClient(name = "api-Socio-service")
public interface SocioClient {
    @GetMapping("/api/v1/socios/asociacion")
    SocioDTO obtenerAsociacion();

    @PutMapping("/api/v1/socios/{id}/verificar-actividad")
    void verificarActividad(@PathVariable("id") Integer idSocio);
}
