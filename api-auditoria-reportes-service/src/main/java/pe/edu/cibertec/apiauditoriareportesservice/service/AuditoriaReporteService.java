package pe.edu.cibertec.apiauditoriareportesservice.service;

import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaAnulacionRequest;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaAnulacionResponse;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaEventoRequest;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaEventoResponse;
import pe.edu.cibertec.apiauditoriareportesservice.dto.PadronHabilResponse;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.DeudaPendienteRemoteResponse;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.FlujoCajaRemoteResponse;

import java.time.LocalDate;
import java.util.List;

public interface AuditoriaReporteService {

    AuditoriaEventoResponse registrarEvento(AuditoriaEventoRequest request);

    List<AuditoriaEventoResponse> listarEventos();

    AuditoriaAnulacionResponse registrarAnulacion(AuditoriaAnulacionRequest request);

    List<AuditoriaAnulacionResponse> listarAnulaciones();

    List<PadronHabilResponse> padronHabiles();

    List<DeudaPendienteRemoteResponse> morosidad();

    List<FlujoCajaRemoteResponse> flujoCajaDiario(LocalDate fecha);

    byte[] padronHabilesPdf();

    byte[] morosidadPdf();

    byte[] flujoCajaDiarioPdf(LocalDate fecha);
}
