package pe.edu.cibertec.apipagosservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.cibertec.apipagosservice.dto.PuestoDTO;

import java.util.List;

@FeignClient(name = "api-Puesto-service")
public interface PuestoClient {
    @GetMapping("/puestos/ocupados")
    List<PuestoDTO> getPuestosOcupados();
}
