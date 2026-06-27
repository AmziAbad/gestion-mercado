package pe.edu.cibertec.apitesoreriaservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import pe.edu.cibertec.apitesoreriaservice.entity.Comprobante;
import pe.edu.cibertec.apitesoreriaservice.entity.ConceptoCobro;
import pe.edu.cibertec.apitesoreriaservice.entity.CuotaDeuda;
import pe.edu.cibertec.apitesoreriaservice.entity.EstadoComprobante;
import pe.edu.cibertec.apitesoreriaservice.entity.EstadoCuota;
import pe.edu.cibertec.apitesoreriaservice.entity.EstadoPago;
import pe.edu.cibertec.apitesoreriaservice.entity.EstadoTurno;
import pe.edu.cibertec.apitesoreriaservice.entity.Pago;
import pe.edu.cibertec.apitesoreriaservice.entity.Periodicidad;
import pe.edu.cibertec.apitesoreriaservice.entity.TipoCobro;
import pe.edu.cibertec.apitesoreriaservice.entity.TurnoCaja;
import pe.edu.cibertec.apitesoreriaservice.exception.ConflictoException;
import pe.edu.cibertec.apitesoreriaservice.exception.RecursoNoEncontradoException;
import pe.edu.cibertec.apitesoreriaservice.exception.ReglaNegocioException;
import pe.edu.cibertec.apitesoreriaservice.mapper.TesoreriaMapper;
import pe.edu.cibertec.apitesoreriaservice.remote.client.AuditoriaClient;
import pe.edu.cibertec.apitesoreriaservice.remote.client.PatrimonioClient;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.AuditoriaAnulacionRequest;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.AuditoriaEventoRequest;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.ContratoRemoteResponse;
import pe.edu.cibertec.apitesoreriaservice.remote.dto.SocioRemoteResponse;
import pe.edu.cibertec.apitesoreriaservice.repository.ComprobanteRepository;
import pe.edu.cibertec.apitesoreriaservice.repository.ConceptoCobroRepository;
import pe.edu.cibertec.apitesoreriaservice.repository.CuotaDeudaRepository;
import pe.edu.cibertec.apitesoreriaservice.repository.PagoRepository;
import pe.edu.cibertec.apitesoreriaservice.repository.TurnoCajaRepository;
import pe.edu.cibertec.apitesoreriaservice.service.TesoreriaService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class TesoreriaServiceImpl implements TesoreriaService {

    private final ConceptoCobroRepository conceptoRepository;
    private final TurnoCajaRepository turnoRepository;
    private final CuotaDeudaRepository cuotaRepository;
    private final PagoRepository pagoRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final PatrimonioClient patrimonioClient;
    private final AuditoriaClient auditoriaClient;

    public TesoreriaServiceImpl(ConceptoCobroRepository conceptoRepository,
                                TurnoCajaRepository turnoRepository,
                                CuotaDeudaRepository cuotaRepository,
                                PagoRepository pagoRepository,
                                ComprobanteRepository comprobanteRepository,
                                PatrimonioClient patrimonioClient,
                                AuditoriaClient auditoriaClient) {
        this.conceptoRepository = conceptoRepository;
        this.turnoRepository = turnoRepository;
        this.cuotaRepository = cuotaRepository;
        this.pagoRepository = pagoRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.patrimonioClient = patrimonioClient;
        this.auditoriaClient = auditoriaClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConceptoResponse> listarConceptos() {
        return conceptoRepository.findAll().stream().map(TesoreriaMapper::toConceptoResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConceptoResponse buscarConcepto(Integer idConcepto) {
        return TesoreriaMapper.toConceptoResponse(obtenerConcepto(idConcepto));
    }

    @Override
    @Transactional
    public ConceptoResponse registrarConcepto(ConceptoRequest request) {
        if (conceptoRepository.existsByNombreConceptoIgnoreCase(request.nombreConcepto())) {
            throw new ConflictoException("Ya existe un concepto con ese nombre.");
        }
        validarImportesConcepto(request);
        ConceptoCobro concepto = ConceptoCobro.builder()
                .nombreConcepto(request.nombreConcepto())
                .descripcion(request.descripcion())
                .tipoCobro(request.tipoCobro())
                .periodicidad(request.periodicidad())
                .montoFijo(request.montoFijo())
                .costoTotalProrrateo(request.costoTotalProrrateo())
                .activo(request.activo() == null || request.activo())
                .build();
        return TesoreriaMapper.toConceptoResponse(conceptoRepository.save(concepto));
    }

    @Override
    @Transactional
    public ConceptoResponse actualizarConcepto(Integer idConcepto, ConceptoRequest request) {
        ConceptoCobro concepto = obtenerConcepto(idConcepto);
        conceptoRepository.findAll().stream()
                .filter(actual -> !actual.getIdConcepto().equals(idConcepto))
                .filter(actual -> actual.getNombreConcepto().equalsIgnoreCase(request.nombreConcepto()))
                .findFirst()
                .ifPresent(actual -> {
                    throw new ConflictoException("Ya existe otro concepto con ese nombre.");
                });
        validarImportesConcepto(request);

        concepto.setNombreConcepto(request.nombreConcepto());
        concepto.setDescripcion(request.descripcion());
        concepto.setTipoCobro(request.tipoCobro());
        concepto.setPeriodicidad(request.periodicidad());
        concepto.setMontoFijo(request.montoFijo());
        concepto.setCostoTotalProrrateo(request.costoTotalProrrateo());
        concepto.setActivo(request.activo() == null || request.activo());
        return TesoreriaMapper.toConceptoResponse(conceptoRepository.save(concepto));
    }

    @Override
    @Transactional
    public ConceptoResponse cambiarEstadoConcepto(Integer idConcepto, Boolean activo) {
        ConceptoCobro concepto = obtenerConcepto(idConcepto);
        concepto.setActivo(Boolean.TRUE.equals(activo));
        return TesoreriaMapper.toConceptoResponse(conceptoRepository.save(concepto));
    }

    @Override
    @Transactional
    public TurnoResponse aperturarTurno(TurnoAperturaRequest request, Integer idUsuario) {
        turnoRepository.findFirstByIdUsuarioAndEstadoTurno(idUsuario, EstadoTurno.ABIERTO)
                .ifPresent(turno -> {
                    throw new ConflictoException("El usuario ya tiene un turno de caja abierto.");
                });
        BigDecimal montoInicial = request.montoInicial() != null ? request.montoInicial() : BigDecimal.ZERO;
        TurnoCaja turno = TurnoCaja.builder()
                .idUsuario(idUsuario)
                .fechaApertura(LocalDateTime.now())
                .montoInicial(montoInicial)
                .montoRecaudado(montoInicial)
                .montoEsperado(montoInicial)
                .diferencia(BigDecimal.ZERO)
                .estadoTurno(EstadoTurno.ABIERTO)
                .observacionApertura(request.observacionApertura())
                .build();
        return TesoreriaMapper.toTurnoResponse(turnoRepository.save(turno));
    }

    @Override
    @Transactional
    public TurnoResponse cerrarTurno(Integer idTurno, TurnoCierreRequest request) {
        TurnoCaja turno = obtenerTurno(idTurno);
        if (!EstadoTurno.ABIERTO.equals(turno.getEstadoTurno())) {
            throw new ReglaNegocioException("Solo se puede cerrar un turno abierto.");
        }
        BigDecimal totalEsperado = turno.getMontoInicial().add(sumarPagosRegistrados(idTurno));
        turno.setMontoRecaudado(request.montoRecaudado());
        turno.setMontoEsperado(totalEsperado);
        turno.setDiferencia(turno.getMontoRecaudado().subtract(turno.getMontoEsperado()));
        turno.setEstadoTurno(EstadoTurno.CERRADO);
        turno.setFechaCierre(LocalDateTime.now());
        turno.setObservacionCierre(request.observacionCierre());
        return TesoreriaMapper.toTurnoResponse(turnoRepository.save(turno));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> listarTurnosPorFecha(LocalDate fecha) {
        LocalDate consulta = fecha != null ? fecha : LocalDate.now();
        return turnoRepository.findByFechaAperturaBetween(consulta.atStartOfDay(), consulta.plusDays(1).atStartOfDay())
                .stream()
                .map(TesoreriaMapper::toTurnoResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<CuotaResponse> generarCuotasMasivas(CuotaMasivaRequest request, Integer idUsuario) {
        ConceptoCobro concepto = obtenerConceptoActivo(request.idConcepto());
        List<ContratoRemoteResponse> contratos = patrimonioClient.listarContratosActivos();
        if (contratos.isEmpty()) {
            throw new ReglaNegocioException("No existen contratos activos cobrables.");
        }

        BigDecimal monto = calcularMontoMasivo(concepto, contratos.size());
        LocalDate vencimiento = request.fechaVencimiento() != null
                ? request.fechaVencimiento()
                : YearMonth.of(request.periodoAnio(), request.periodoMes()).atEndOfMonth();

        List<CuotaResponse> generadas = new ArrayList<>();
        for (ContratoRemoteResponse contrato : contratos) {
            boolean existe = cuotaRepository.existsByIdContratoAndIdConceptoAndPeriodoMesAndPeriodoAnio(
                    contrato.idContrato(),
                    concepto.getIdConcepto(),
                    request.periodoMes(),
                    request.periodoAnio()
            );
            if (existe) {
                continue;
            }
            CuotaDeuda cuota = crearCuota(
                    contrato.idPuesto(),
                    contrato.idContrato(),
                    concepto.getIdConcepto(),
                    request.periodoMes(),
                    request.periodoAnio(),
                    monto,
                    vencimiento,
                    idUsuario
            );
            generadas.add(TesoreriaMapper.toCuotaResponse(cuotaRepository.save(cuota)));
        }
        return generadas;
    }

    @Override
    @Transactional
    public CuotaResponse generarCuotaEspecifica(CuotaEspecificaRequest request, Integer idUsuario) {
        patrimonioClient.buscarPuesto(request.idPuesto());
        ConceptoCobro concepto = obtenerConceptoActivo(request.idConcepto());
        Integer idContrato = request.idContrato() != null
                ? patrimonioClient.buscarContrato(request.idContrato()).idContrato()
                : patrimonioClient.buscarContratoActivoPorPuesto(request.idPuesto()).idContrato();
        BigDecimal monto = request.montoTotal() != null ? request.montoTotal() : calcularMontoMasivo(concepto, 1);

        CuotaDeuda cuota = crearCuota(
                request.idPuesto(),
                idContrato,
                concepto.getIdConcepto(),
                request.periodoMes(),
                request.periodoAnio(),
                monto,
                request.fechaVencimiento(),
                idUsuario
        );
        return TesoreriaMapper.toCuotaResponse(cuotaRepository.save(cuota));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuotaResponse> listarCuotas() {
        return cuotaRepository.findAll().stream().map(TesoreriaMapper::toCuotaResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CuotaResponse buscarCuota(Integer idCuota) {
        return TesoreriaMapper.toCuotaResponse(obtenerCuota(idCuota));
    }

    @Override
    @Transactional
    public CuotaResponse anularCuota(Integer idCuota, CuotaAnulacionRequest request, Integer idUsuario) {
        CuotaDeuda cuota = obtenerCuota(idCuota);
        if (EstadoCuota.PAGADO.equals(cuota.getEstadoCuota())) {
            throw new ReglaNegocioException("No se puede anular una cuota pagada. Primero debe extornarse el pago.");
        }
        if (!EstadoCuota.PENDIENTE.equals(cuota.getEstadoCuota())) {
            throw new ReglaNegocioException("Solo se puede anular una cuota pendiente.");
        }

        cuota.setEstadoCuota(EstadoCuota.ANULADO);
        cuota.setMotivoAnulacion(request.motivoAnulacion());
        cuota.setFechaAnulacion(LocalDateTime.now());
        cuota.setIdUsuarioAnulacion(idUsuario);

        if (Boolean.TRUE.equals(request.generarReemplazo())) {
            if (request.montoReemplazo() == null) {
                throw new ReglaNegocioException("Debe indicar el monto de reemplazo.");
            }
            CuotaDeuda reemplazo = crearCuota(
                    cuota.getIdPuesto(),
                    cuota.getIdContrato(),
                    cuota.getIdConcepto(),
                    cuota.getPeriodoMes(),
                    cuota.getPeriodoAnio(),
                    request.montoReemplazo(),
                    cuota.getFechaVencimiento(),
                    idUsuario
            );
            reemplazo.setIdCuotaOrigen(cuota.getIdCuota());
            reemplazo = cuotaRepository.save(reemplazo);
            cuota.setIdCuotaReemplazo(reemplazo.getIdCuota());
        }

        cuota = cuotaRepository.save(cuota);
        registrarAnulacion("CUOTA", cuota.getIdCuota(), idUsuario, request.motivoAnulacion());
        registrarEvento("TESORERIA", "CUOTA_ANULADA", "cuotas_deuda", cuota.getIdCuota(), idUsuario,
                "Cuota anulada con sustento.");
        return TesoreriaMapper.toCuotaResponse(cuota);
    }

    @Override
    @Transactional
    public CuotaResponse exonerarCuota(Integer idCuota, CuotaExoneracionRequest request, Integer idUsuario) {
        CuotaDeuda cuota = obtenerCuota(idCuota);
        if (!EstadoCuota.PENDIENTE.equals(cuota.getEstadoCuota())) {
            throw new ReglaNegocioException("Solo se puede exonerar una cuota pendiente.");
        }
        cuota.setEstadoCuota(EstadoCuota.EXONERADO);
        cuota.setMotivoExoneracion(request.motivoExoneracion());
        cuota.setFechaExoneracion(LocalDateTime.now());
        cuota.setIdUsuarioExoneracion(idUsuario);
        cuota = cuotaRepository.save(cuota);
        registrarEvento("TESORERIA", "CUOTA_EXONERADA", "cuotas_deuda", cuota.getIdCuota(), idUsuario,
                "Cuota exonerada con motivo registrado.");
        return TesoreriaMapper.toCuotaResponse(cuota);
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoCuentaResponse estadoCuentaPorPuesto(Integer idPuesto) {
        patrimonioClient.buscarPuesto(idPuesto);
        List<CuotaResponse> cuotas = cuotaRepository.findByIdPuestoAndEstadoCuota(idPuesto, EstadoCuota.PENDIENTE)
                .stream()
                .map(TesoreriaMapper::toCuotaResponse)
                .toList();
        return new EstadoCuentaResponse(idPuesto, null, cuotas.size(), totalCuotas(cuotas), cuotas);
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoCuentaResponse estadoCuentaPorCodigoPuesto(String codigoPuesto) {
        return estadoCuentaPorPuesto(patrimonioClient.buscarPuestoPorCodigo(codigoPuesto).idPuesto());
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoCuentaResponse estadoCuentaPorSocio(Integer idSocio) {
        List<ContratoRemoteResponse> contratos = patrimonioClient.listarContratosActivosPorSocio(idSocio);
        List<CuotaResponse> cuotas = contratos.stream()
                .flatMap(contrato -> cuotaRepository.findByIdPuestoAndEstadoCuota(
                        contrato.idPuesto(),
                        EstadoCuota.PENDIENTE
                ).stream())
                .map(TesoreriaMapper::toCuotaResponse)
                .toList();
        return new EstadoCuentaResponse(null, idSocio, cuotas.size(), totalCuotas(cuotas), cuotas);
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoCuentaResponse estadoCuentaPorDni(String dni) {
        SocioRemoteResponse socio = patrimonioClient.buscarSocioPorDni(dni);
        return estadoCuentaPorSocio(socio.idSocio());
    }

    @Override
    @Transactional(readOnly = true)
    public DeudaResumenResponse resumenDeudaPorPuesto(Integer idPuesto) {
        List<CuotaDeuda> cuotas = cuotaRepository.findByIdPuestoAndEstadoCuota(idPuesto, EstadoCuota.PENDIENTE);
        BigDecimal total = cuotas.stream()
                .map(CuotaDeuda::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DeudaResumenResponse(idPuesto, cuotas.size(), total);
    }

    @Override
    @Transactional
    public PagoResponse registrarPago(PagoRequest request, Integer idUsuario) {
        TurnoCaja turno = turnoRepository.findFirstByIdUsuarioAndEstadoTurno(idUsuario, EstadoTurno.ABIERTO)
                .orElseThrow(() -> new ReglaNegocioException("El usuario no tiene turno de caja abierto."));
        CuotaDeuda cuota = obtenerCuota(request.idCuota());
        if (!EstadoCuota.PENDIENTE.equals(cuota.getEstadoCuota())) {
            throw new ReglaNegocioException("Solo se puede pagar una cuota pendiente.");
        }

        Pago pago = Pago.builder()
                .idCuota(cuota.getIdCuota())
                .idTurno(turno.getIdTurno())
                .idUsuarioCobro(idUsuario)
                .metodoPago(request.metodoPago())
                .numeroOperacion(request.numeroOperacion())
                .montoPagado(cuota.getMontoTotal())
                .fechaPago(LocalDateTime.now())
                .estadoPago(EstadoPago.REGISTRADO)
                .build();
        pago = pagoRepository.save(pago);

        cuota.setEstadoCuota(EstadoCuota.PAGADO);
        cuotaRepository.save(cuota);

        Comprobante comprobante = Comprobante.builder()
                .idPago(pago.getIdPago())
                .idCuota(cuota.getIdCuota())
                .numeroComprobante(generarNumeroComprobante(pago))
                .fechaEmision(LocalDateTime.now())
                .montoTotal(pago.getMontoPagado())
                .metodoPago(pago.getMetodoPago())
                .estadoComprobante(EstadoComprobante.EMITIDO)
                .build();
        comprobante = comprobanteRepository.save(comprobante);

        turno.setMontoRecaudado(turno.getMontoRecaudado().add(pago.getMontoPagado()));
        turno.setMontoEsperado(turno.getMontoEsperado().add(pago.getMontoPagado()));
        turno.setDiferencia(turno.getMontoRecaudado().subtract(turno.getMontoEsperado()));
        turnoRepository.save(turno);

        registrarEvento("TESORERIA", "PAGO_REGISTRADO", "pagos", pago.getIdPago(), idUsuario,
                "Pago total registrado y comprobante emitido.");

        return TesoreriaMapper.toPagoResponse(pago, comprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse buscarPago(Integer idPago) {
        Pago pago = obtenerPago(idPago);
        Comprobante comprobante = comprobanteRepository.findByIdPago(pago.getIdPago()).orElse(null);
        return TesoreriaMapper.toPagoResponse(pago, comprobante);
    }

    @Override
    @Transactional
    public PagoResponse extornarPago(Integer idPago, PagoExtornoRequest request, Integer idUsuario) {
        Pago pago = obtenerPago(idPago);
        if (!EstadoPago.REGISTRADO.equals(pago.getEstadoPago())) {
            throw new ReglaNegocioException("Solo se puede extornar un pago registrado.");
        }
        CuotaDeuda cuota = obtenerCuota(pago.getIdCuota());
        TurnoCaja turno = obtenerTurno(pago.getIdTurno());
        Comprobante comprobante = comprobanteRepository.findByIdPago(pago.getIdPago())
                .orElseThrow(() -> new RecursoNoEncontradoException("Comprobante no encontrado para el pago."));

        pago.setEstadoPago(EstadoPago.EXTORNADO);
        pago.setMotivoExtorno(request.motivoExtorno());
        pago.setFechaExtorno(LocalDateTime.now());
        pago.setIdUsuarioExtorno(idUsuario);
        pago = pagoRepository.save(pago);

        cuota.setEstadoCuota(EstadoCuota.PENDIENTE);
        cuotaRepository.save(cuota);

        comprobante.setEstadoComprobante(EstadoComprobante.ANULADO);
        comprobante = comprobanteRepository.save(comprobante);

        turno.setMontoEsperado(turno.getMontoEsperado().subtract(pago.getMontoPagado()));
        if (EstadoTurno.ABIERTO.equals(turno.getEstadoTurno())) {
            turno.setMontoRecaudado(turno.getMontoRecaudado().subtract(pago.getMontoPagado()));
        }
        turno.setDiferencia(turno.getMontoRecaudado().subtract(turno.getMontoEsperado()));
        turnoRepository.save(turno);

        registrarAnulacion("PAGO", pago.getIdPago(), idUsuario, request.motivoExtorno());
        registrarAnulacion("COMPROBANTE", comprobante.getIdComprobante(), idUsuario, request.motivoExtorno());
        registrarEvento("TESORERIA", "PAGO_EXTORNADO", "pagos", pago.getIdPago(), idUsuario,
                "Pago extornado con sustento.");

        return TesoreriaMapper.toPagoResponse(pago, comprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteResponse buscarComprobante(Integer idComprobante) {
        return TesoreriaMapper.toComprobanteResponse(comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comprobante no encontrado.")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteResponse> listarComprobantes() {
        return comprobanteRepository.findAll().stream()
                .map(TesoreriaMapper::toComprobanteResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteResponse buscarComprobantePorPago(Integer idPago) {
        return TesoreriaMapper.toComprobanteResponse(comprobanteRepository.findByIdPago(idPago)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comprobante no encontrado para el pago.")));
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteResponse buscarComprobantePorCuota(Integer idCuota) {
        return TesoreriaMapper.toComprobanteResponse(comprobanteRepository.findByIdCuota(idCuota)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comprobante no encontrado para la cuota.")));
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteResponse buscarComprobantePorNumero(String numeroComprobante) {
        return TesoreriaMapper.toComprobanteResponse(comprobanteRepository.findByNumeroComprobante(numeroComprobante)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comprobante no encontrado.")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeudaPendienteReporteResponse> deudasVencidas() {
        return cuotaRepository.findByEstadoCuotaAndFechaVencimientoBefore(EstadoCuota.PENDIENTE, LocalDate.now()).stream()
                .map(TesoreriaMapper::toDeudaPendiente)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlujoCajaReporteResponse> flujoCajaDiario(LocalDate fecha, Integer idTurno) {
        if (idTurno != null) {
            obtenerTurno(idTurno);
            return pagoRepository.findByIdTurnoAndEstadoPago(idTurno, EstadoPago.REGISTRADO).stream()
                    .filter(pago -> fecha == null || pago.getFechaPago().toLocalDate().equals(fecha))
                    .map(TesoreriaMapper::toFlujoCaja)
                    .toList();
        }
        LocalDate consulta = fecha != null ? fecha : LocalDate.now();
        return pagoRepository.findByFechaPagoBetween(consulta.atStartOfDay(), consulta.plusDays(1).atStartOfDay())
                .stream()
                .filter(pago -> EstadoPago.REGISTRADO.equals(pago.getEstadoPago()))
                .map(TesoreriaMapper::toFlujoCaja)
                .toList();
    }

    private CuotaDeuda crearCuota(Integer idPuesto, Integer idContrato, Integer idConcepto, Integer mes, Integer anio,
                                  BigDecimal monto, LocalDate vencimiento, Integer idUsuario) {
        return CuotaDeuda.builder()
                .idPuesto(idPuesto)
                .idContrato(idContrato)
                .idConcepto(idConcepto)
                .periodoMes(mes)
                .periodoAnio(anio)
                .montoTotal(monto)
                .estadoCuota(EstadoCuota.PENDIENTE)
                .fechaGeneracion(LocalDateTime.now())
                .fechaVencimiento(vencimiento)
                .idUsuarioGeneracion(idUsuario)
                .build();
    }

    private void validarImportesConcepto(ConceptoRequest request) {
        if (TipoCobro.FIJO.equals(request.tipoCobro()) && request.montoFijo() == null) {
            throw new ReglaNegocioException("Los conceptos fijos requieren monto fijo.");
        }
        if (TipoCobro.PRORRATEO.equals(request.tipoCobro()) && request.costoTotalProrrateo() == null) {
            throw new ReglaNegocioException("Los conceptos prorrateados requieren costo total.");
        }
        if (Periodicidad.EXTRAORDINARIO.equals(request.periodicidad()) && TipoCobro.PRORRATEO.equals(request.tipoCobro())) {
            throw new ReglaNegocioException("Los cobros extraordinarios deben registrarse como monto fijo.");
        }
    }

    private BigDecimal calcularMontoMasivo(ConceptoCobro concepto, int cantidadContratos) {
        if (TipoCobro.PRORRATEO.equals(concepto.getTipoCobro())) {
            return concepto.getCostoTotalProrrateo().divide(BigDecimal.valueOf(cantidadContratos), 2, RoundingMode.HALF_UP);
        }
        return concepto.getMontoFijo();
    }

    private BigDecimal totalCuotas(List<CuotaResponse> cuotas) {
        return cuotas.stream().map(CuotaResponse::montoTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumarPagosRegistrados(Integer idTurno) {
        return pagoRepository.findByIdTurnoAndEstadoPago(idTurno, EstadoPago.REGISTRADO).stream()
                .map(Pago::getMontoPagado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ConceptoCobro obtenerConcepto(Integer idConcepto) {
        return conceptoRepository.findById(idConcepto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Concepto de cobro no encontrado."));
    }

    private ConceptoCobro obtenerConceptoActivo(Integer idConcepto) {
        ConceptoCobro concepto = obtenerConcepto(idConcepto);
        if (!Boolean.TRUE.equals(concepto.getActivo())) {
            throw new ReglaNegocioException("El concepto de cobro se encuentra inactivo.");
        }
        return concepto;
    }

    private TurnoCaja obtenerTurno(Integer idTurno) {
        return turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno de caja no encontrado."));
    }

    private CuotaDeuda obtenerCuota(Integer idCuota) {
        return cuotaRepository.findById(idCuota)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuota no encontrada."));
    }

    private Pago obtenerPago(Integer idPago) {
        return pagoRepository.findById(idPago)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado."));
    }

    private String generarNumeroComprobante(Pago pago) {
        return "REC-" + LocalDate.now().toString().replace("-", "") + "-" + String.format("%06d", pago.getIdPago());
    }

    private void registrarEvento(String modulo, String tipoEvento, String entidad, Integer idRegistro,
                                 Integer idUsuario, String descripcion) {
        try {
            auditoriaClient.registrarEvento(new AuditoriaEventoRequest(
                    modulo, tipoEvento, entidad, idRegistro, idUsuario, descripcion
            ));
        } catch (Exception ignored) {
            // La trazabilidad minima queda en el registro financiero local.
        }
    }

    private void registrarAnulacion(String tipo, Integer idRegistro, Integer idUsuario, String motivo) {
        try {
            auditoriaClient.registrarAnulacion(new AuditoriaAnulacionRequest(tipo, idRegistro, idUsuario, motivo));
        } catch (Exception ignored) {
            // La anulacion ya queda sustentada en la entidad financiera local.
        }
    }
}
