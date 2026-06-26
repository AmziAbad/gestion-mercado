package pe.edu.cibertec.apitesoreriaservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.AuditoriaAnulacionRequest;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.AuditoriaEventoRequest;

@FeignClient(name = "api-auditoria-reportes-service")
public interface AuditoriaClient {

    @PostMapping("/api/v1/auditoria/eventos")
    void registrarEvento(@RequestBody AuditoriaEventoRequest request);

    @PostMapping("/api/v1/auditoria/anulaciones")
    void registrarAnulacion(@RequestBody AuditoriaAnulacionRequest request);
}
