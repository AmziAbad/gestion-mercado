package pe.edu.cibertec.apiauditoriareportesservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.DeudaPendienteRemoteResponse;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.EstadoCuentaRemoteResponse;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.FlujoCajaRemoteResponse;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "api-tesoreria-service")
public interface TesoreriaClient {

    @GetMapping("/estados-cuenta/socio/{idSocio}")
    EstadoCuentaRemoteResponse estadoCuentaPorSocio(@PathVariable Integer idSocio);

    @GetMapping("/reportes/morosidad/datos")
    List<DeudaPendienteRemoteResponse> morosidad();

    @GetMapping("/reportes/flujo-caja-diario/datos")
    List<FlujoCajaRemoteResponse> flujoCaja(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Integer idTurno
    );
}
