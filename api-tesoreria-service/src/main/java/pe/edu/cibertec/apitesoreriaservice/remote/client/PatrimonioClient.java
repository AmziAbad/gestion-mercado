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

    @GetMapping("/contratos/activos")
    List<ContratoRemoteResponse> listarContratosActivos();

    @GetMapping("/contratos/{idContrato}")
    ContratoRemoteResponse buscarContrato(@PathVariable Integer idContrato);

    @GetMapping("/contratos/puesto/{idPuesto}/activo")
    ContratoRemoteResponse buscarContratoActivoPorPuesto(@PathVariable Integer idPuesto);

    @GetMapping("/contratos/socio/{idSocio}/activos")
    List<ContratoRemoteResponse> listarContratosActivosPorSocio(@PathVariable Integer idSocio);

    @GetMapping("/socios/{idSocio}")
    SocioRemoteResponse buscarSocio(@PathVariable Integer idSocio);

    @GetMapping("/socios/dni/{dni}")
    SocioRemoteResponse buscarSocioPorDni(@PathVariable String dni);

    @GetMapping("/puestos/{idPuesto}")
    PuestoRemoteResponse buscarPuesto(@PathVariable Integer idPuesto);
}
