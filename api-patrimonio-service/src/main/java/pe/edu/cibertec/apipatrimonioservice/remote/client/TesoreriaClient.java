package pe.edu.cibertec.apipatrimonioservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.apipatrimonioservice.remote.dto.DeudaResumenResponse;

@FeignClient(name = "api-tesoreria-service")
public interface TesoreriaClient {

    @GetMapping("/estados-cuenta/puesto/{idPuesto}/resumen")
    DeudaResumenResponse obtenerResumenDeudaPuesto(@PathVariable Integer idPuesto);
}
