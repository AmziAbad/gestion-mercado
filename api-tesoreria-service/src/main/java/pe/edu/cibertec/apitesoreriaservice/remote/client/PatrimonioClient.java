package pe.edu.cibertec.apitesoreriaservice.remote.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.ContratoRemoteResponse;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.PuestoRemoteResponse;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.SocioRemoteResponse;

import java.util.List;

@FeignClient(name = "api-patrimonio-service")
public interface PatrimonioClient {

    @GetMapping("/api/v1/contratos/activos")
    List<ContratoRemoteResponse> listarContratosActivos();

    @GetMapping("/api/v1/contratos/{idContrato}")
    ContratoRemoteResponse buscarContrato(@PathVariable Integer idContrato);

    @GetMapping("/api/v1/contratos/puesto/{idPuesto}/activo")
    ContratoRemoteResponse buscarContratoActivoPorPuesto(@PathVariable Integer idPuesto);

    @GetMapping("/api/v1/contratos/socio/{idSocio}/activos")
    List<ContratoRemoteResponse> listarContratosActivosPorSocio(@PathVariable Integer idSocio);

    @GetMapping("/api/v1/socios/{idSocio}")
    SocioRemoteResponse buscarSocio(@PathVariable Integer idSocio);

    @GetMapping("/api/v1/socios/dni/{dni}")
    SocioRemoteResponse buscarSocioPorDni(@PathVariable String dni);

    @GetMapping("/api/v1/puestos/{idPuesto}")
    PuestoRemoteResponse buscarPuesto(@PathVariable Integer idPuesto);
}
