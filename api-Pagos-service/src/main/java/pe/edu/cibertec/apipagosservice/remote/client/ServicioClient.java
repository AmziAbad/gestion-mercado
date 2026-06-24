package pe.edu.cibertec.apipagosservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.apipagosservice.dto.ServicioDTO;

import java.util.List;

@FeignClient(name = "api-Servicio-service")
public interface ServicioClient {
    @GetMapping("/servicios/activos")
    List<ServicioDTO> getServiciosActivos();

    @GetMapping("/api/v1/servicios/{id}")
    ServicioDTO getServicioById(@PathVariable("id") Integer id);
}
