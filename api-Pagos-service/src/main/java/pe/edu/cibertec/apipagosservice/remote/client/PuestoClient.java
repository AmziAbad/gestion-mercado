package pe.edu.cibertec.apipagosservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.apipagosservice.dto.PuestoDTO;

import java.util.List;

@FeignClient(name = "api-Puesto-service")
public interface PuestoClient {
    @GetMapping("/puestos/ocupados")
    List<PuestoDTO> getPuestosOcupados();

    @GetMapping("/api/v1/puestos/{id}")
    PuestoDTO getPuestoById(@PathVariable("id") Integer id);

    @GetMapping("/puestos/socio/{idSocio}")
    List<PuestoDTO> getPuestosPorSocio(@PathVariable Integer idSocio);
}
