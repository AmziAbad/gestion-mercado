package pe.edu.cibertec.apiauditoriareportesservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaAnulacionRequest;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaAnulacionResponse;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaEventoRequest;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaEventoResponse;
import pe.edu.cibertec.apiauditoriareportesservice.dto.PadronHabilResponse;
import pe.edu.cibertec.apiauditoriareportesservice.dto.ReporteFila;
import pe.edu.cibertec.apiauditoriareportesservice.entity.AuditoriaAnulacion;
import pe.edu.cibertec.apiauditoriareportesservice.entity.AuditoriaEvento;
import pe.edu.cibertec.apiauditoriareportesservice.mapper.AuditoriaMapper;
import pe.edu.cibertec.apiauditoriareportesservice.remote.client.PatrimonioClient;
import pe.edu.cibertec.apiauditoriareportesservice.remote.client.TesoreriaClient;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.DeudaPendienteRemoteResponse;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.FlujoCajaRemoteResponse;
import pe.edu.cibertec.apiauditoriareportesservice.remote.dto.SocioRemoteResponse;
import pe.edu.cibertec.apiauditoriareportesservice.report.JasperReporteService;
import pe.edu.cibertec.apiauditoriareportesservice.repository.AuditoriaAnulacionRepository;
import pe.edu.cibertec.apiauditoriareportesservice.repository.AuditoriaEventoRepository;
import pe.edu.cibertec.apiauditoriareportesservice.service.AuditoriaReporteService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaReporteServiceImpl implements AuditoriaReporteService {

    private final AuditoriaEventoRepository eventoRepository;
    private final AuditoriaAnulacionRepository anulacionRepository;
    private final PatrimonioClient patrimonioClient;
    private final TesoreriaClient tesoreriaClient;
    private final JasperReporteService jasperReporteService;

    public AuditoriaReporteServiceImpl(AuditoriaEventoRepository eventoRepository,
                                       AuditoriaAnulacionRepository anulacionRepository,
                                       PatrimonioClient patrimonioClient,
                                       TesoreriaClient tesoreriaClient,
                                       JasperReporteService jasperReporteService) {
        this.eventoRepository = eventoRepository;
        this.anulacionRepository = anulacionRepository;
        this.patrimonioClient = patrimonioClient;
        this.tesoreriaClient = tesoreriaClient;
        this.jasperReporteService = jasperReporteService;
    }

    @Override
    @Transactional
    public AuditoriaEventoResponse registrarEvento(AuditoriaEventoRequest request) {
        AuditoriaEvento evento = AuditoriaEvento.builder()
                .modulo(request.modulo())
                .tipoEvento(request.tipoEvento())
                .entidadAfectada(request.entidadAfectada())
                .idRegistroAfectado(request.idRegistroAfectado())
                .idUsuario(request.idUsuario())
                .descripcion(request.descripcion())
                .fechaEvento(LocalDateTime.now())
                .build();
        return AuditoriaMapper.toEventoResponse(eventoRepository.save(evento));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaEventoResponse> listarEventos() {
        return eventoRepository.findAll().stream().map(AuditoriaMapper::toEventoResponse).toList();
    }

    @Override
    @Transactional
    public AuditoriaAnulacionResponse registrarAnulacion(AuditoriaAnulacionRequest request) {
        AuditoriaAnulacion anulacion = AuditoriaAnulacion.builder()
                .tipoAnulacion(request.tipoAnulacion())
                .idRegistroAfectado(request.idRegistroAfectado())
                .idUsuario(request.idUsuario())
                .motivoSustento(request.motivoSustento())
                .fechaAnulacion(LocalDateTime.now())
                .build();
        return AuditoriaMapper.toAnulacionResponse(anulacionRepository.save(anulacion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaAnulacionResponse> listarAnulaciones() {
        return anulacionRepository.findAll().stream().map(AuditoriaMapper::toAnulacionResponse).toList();
    }

    @Override
    public List<PadronHabilResponse> padronHabiles() {
        return patrimonioClient.listarSociosActivosConContrato().stream()
                .filter(socio -> deudaSocio(socio.idSocio()).compareTo(BigDecimal.ZERO) == 0)
                .map(socio -> new PadronHabilResponse(
                        socio.idSocio(),
                        socio.dni(),
                        socio.nombres(),
                        socio.apellidos(),
                        socio.correo(),
                        socio.telefono()
                ))
                .toList();
    }

    @Override
    public List<DeudaPendienteRemoteResponse> morosidad() {
        return tesoreriaClient.morosidad();
    }

    @Override
    public List<FlujoCajaRemoteResponse> flujoCajaDiario(LocalDate fecha) {
        return tesoreriaClient.flujoCaja(fecha);
    }

    @Override
    public byte[] padronHabilesPdf() {
        List<ReporteFila> filas = padronHabiles().stream()
                .map(socio -> new ReporteFila(
                        socio.dni(),
                        socio.nombres() + " " + socio.apellidos(),
                        "HABIL"
                ))
                .toList();
        return jasperReporteService.generarPdf("Padron de Habiles", filas);
    }

    @Override
    public byte[] morosidadPdf() {
        List<ReporteFila> filas = morosidad().stream()
                .map(deuda -> new ReporteFila(
                        "Puesto " + deuda.idPuesto(),
                        "Cuota " + deuda.idCuota() + " - periodo " + deuda.periodoMes() + "/" + deuda.periodoAnio(),
                        "S/ " + deuda.montoTotal()
                ))
                .toList();
        return jasperReporteService.generarPdf("Reporte de Morosidad", filas);
    }

    @Override
    public byte[] flujoCajaDiarioPdf(LocalDate fecha) {
        List<ReporteFila> filas = flujoCajaDiario(fecha).stream()
                .map(pago -> new ReporteFila(
                        "Pago " + pago.idPago(),
                        "Turno " + pago.idTurno() + " - " + pago.metodoPago(),
                        "S/ " + pago.montoPagado()
                ))
                .toList();
        return jasperReporteService.generarPdf("Flujo de Caja Diario", filas);
    }

    private BigDecimal deudaSocio(Integer idSocio) {
        return tesoreriaClient.estadoCuentaPorSocio(idSocio).totalPendiente();
    }
}
