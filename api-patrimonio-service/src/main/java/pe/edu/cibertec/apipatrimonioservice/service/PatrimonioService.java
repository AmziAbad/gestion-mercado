package pe.edu.cibertec.apipatrimonioservice.service;

import pe.edu.cibertec.apipatrimonioservice.dto.ContratoRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.ContratoResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.FinalizarContratoRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.PuestoRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.PuestoResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.SaneamientoResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.SocioRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.SocioResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.TransferenciaRequest;
import pe.edu.cibertec.apipatrimonioservice.dto.TransferenciaResponse;

import java.util.List;

public interface PatrimonioService {

    List<SocioResponse> listarSocios();

    List<SocioResponse> listarSociosActivosConContrato();

    SocioResponse buscarSocio(Integer idSocio);

    SocioResponse buscarSocioPorDni(String dni);

    SocioResponse registrarSocio(SocioRequest request);

    SocioResponse actualizarSocio(Integer idSocio, SocioRequest request);

    List<PuestoResponse> listarPuestos();

    PuestoResponse buscarPuesto(Integer idPuesto);

    PuestoResponse registrarPuesto(PuestoRequest request);

    PuestoResponse actualizarPuesto(Integer idPuesto, PuestoRequest request);

    List<ContratoResponse> listarContratosActivos();

    ContratoResponse buscarContrato(Integer idContrato);

    ContratoResponse buscarContratoActivoPorPuesto(Integer idPuesto);

    List<ContratoResponse> listarContratosActivosPorSocio(Integer idSocio);

    ContratoResponse aperturarContrato(ContratoRequest request, Integer idUsuario);

    ContratoResponse finalizarContrato(Integer idContrato, FinalizarContratoRequest request);

    SaneamientoResponse auditarSaneamiento(Integer idPuesto);

    List<TransferenciaResponse> listarTransferencias();

    TransferenciaResponse buscarTransferencia(Integer idTransferencia);

    TransferenciaResponse ejecutarTransferencia(TransferenciaRequest request, Integer idUsuario);
}
