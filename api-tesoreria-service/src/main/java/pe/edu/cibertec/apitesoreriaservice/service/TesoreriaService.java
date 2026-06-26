package pe.edu.cibertec.apitesoreriaservice.service;

import pe.edu.cibertec.apitesoreriaservice.dto.ComprobanteResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.ConceptoRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.ConceptoResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaAnulacionRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaEspecificaRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaExoneracionRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaMasivaRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.DeudaPendienteReporteResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.DeudaResumenResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.EstadoCuentaResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.FlujoCajaReporteResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.PagoExtornoRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.PagoRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.PagoResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.TurnoAperturaRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.TurnoCierreRequest;
import pe.edu.cibertec.apitesoreriaservice.dto.TurnoResponse;

import java.time.LocalDate;
import java.util.List;

public interface TesoreriaService {

    List<ConceptoResponse> listarConceptos();

    ConceptoResponse buscarConcepto(Integer idConcepto);

    ConceptoResponse registrarConcepto(ConceptoRequest request);

    ConceptoResponse actualizarConcepto(Integer idConcepto, ConceptoRequest request);

    ConceptoResponse cambiarEstadoConcepto(Integer idConcepto, Boolean activo);

    TurnoResponse aperturarTurno(TurnoAperturaRequest request, Integer idUsuario);

    TurnoResponse cerrarTurno(Integer idTurno, TurnoCierreRequest request);

    List<TurnoResponse> listarTurnosPorFecha(LocalDate fecha);

    List<CuotaResponse> generarCuotasMasivas(CuotaMasivaRequest request, Integer idUsuario);

    CuotaResponse generarCuotaEspecifica(CuotaEspecificaRequest request, Integer idUsuario);

    List<CuotaResponse> listarCuotas();

    CuotaResponse buscarCuota(Integer idCuota);

    CuotaResponse anularCuota(Integer idCuota, CuotaAnulacionRequest request, Integer idUsuario);

    CuotaResponse exonerarCuota(Integer idCuota, CuotaExoneracionRequest request, Integer idUsuario);

    EstadoCuentaResponse estadoCuentaPorPuesto(Integer idPuesto);

    EstadoCuentaResponse estadoCuentaPorSocio(Integer idSocio);

    EstadoCuentaResponse estadoCuentaPorDni(String dni);

    DeudaResumenResponse resumenDeudaPorPuesto(Integer idPuesto);

    PagoResponse registrarPago(PagoRequest request, Integer idUsuario);

    PagoResponse buscarPago(Integer idPago);

    PagoResponse extornarPago(Integer idPago, PagoExtornoRequest request, Integer idUsuario);

    ComprobanteResponse buscarComprobante(Integer idComprobante);

    ComprobanteResponse buscarComprobantePorPago(Integer idPago);

    List<DeudaPendienteReporteResponse> deudasPendientes();

    List<FlujoCajaReporteResponse> flujoCajaDiario(LocalDate fecha);
}
