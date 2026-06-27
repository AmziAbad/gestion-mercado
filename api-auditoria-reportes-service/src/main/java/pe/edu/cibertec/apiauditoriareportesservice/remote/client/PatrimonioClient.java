package pe.edu.cibertec.apiauditoriareportesservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.SocioRemoteResponse;

import java.util.List;

@FeignClient(name = "api-patrimonio-service")
public interface PatrimonioClient {

    @GetMapping("/socios/activos-con-contrato")
    List<SocioRemoteResponse> listarSociosActivosConContrato();
}
