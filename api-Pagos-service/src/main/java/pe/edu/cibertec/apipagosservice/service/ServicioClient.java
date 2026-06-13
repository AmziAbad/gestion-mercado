package pe.edu.cibertec.apipagosservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.apipagosservice.dto.ServicioDTO;

import java.util.List;

@FeignClient(name = "api-Service-service")
public interface ServicioClient {
    @GetMapping("/servicios/activos")
    List<ServicioDTO> getServiciosActivos();

    @GetMapping("/servicios/{id}")
    ServicioDTO getServicioById(@PathVariable Integer id);
}
