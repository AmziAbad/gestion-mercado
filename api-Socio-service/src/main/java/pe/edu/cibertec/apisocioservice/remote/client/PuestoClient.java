package pe.edu.cibertec.apisocioservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.apisocioservice.dto.PuestoDTO;

import java.util.List;

@FeignClient(name = "api-Puesto-service")
public interface PuestoClient {
    @GetMapping("/api/v1/puestos/socio/{idSocio}")
    List<PuestoDTO> listarPorSocio(@PathVariable Integer idSocio);
}
