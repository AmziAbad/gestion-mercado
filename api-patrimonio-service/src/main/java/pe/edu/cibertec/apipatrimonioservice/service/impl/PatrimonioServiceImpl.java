package pe.edu.cibertec.apipatrimonioservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import pe.edu.cibertec.apipatrimonioservice.entity.ContratoOcupacion;
import pe.edu.cibertec.apipatrimonioservice.entity.EstadoContrato;
import pe.edu.cibertec.apipatrimonioservice.entity.EstadoPuesto;
import pe.edu.cibertec.apipatrimonioservice.entity.EstadoSocio;
import pe.edu.cibertec.apipatrimonioservice.entity.EstadoTransferencia;
import pe.edu.cibertec.apipatrimonioservice.entity.Puesto;
import pe.edu.cibertec.apipatrimonioservice.entity.Socio;
import pe.edu.cibertec.apipatrimonioservice.entity.TransferenciaTitularidad;
import pe.edu.cibertec.apipatrimonioservice.exception.ConflictoException;
import pe.edu.cibertec.apipatrimonioservice.exception.RecursoNoEncontradoException;
import pe.edu.cibertec.apipatrimonioservice.exception.ReglaNegocioException;
import pe.edu.cibertec.apipatrimonioservice.mapper.PatrimonioMapper;
import pe.edu.cibertec.apipatrimonioservice.remote.client.AuditoriaClient;
import pe.edu.cibertec.apipatrimonioservice.remote.client.TesoreriaClient;
import pe.edu.cibertec.apipatrimonioservice.remote.dto.AuditoriaEventoRequest;
import pe.edu.cibertec.apipatrimonioservice.remote.dto.DeudaResumenResponse;
import pe.edu.cibertec.apipatrimonioservice.repository.ContratoOcupacionRepository;
import pe.edu.cibertec.apipatrimonioservice.repository.PuestoRepository;
import pe.edu.cibertec.apipatrimonioservice.repository.SocioRepository;
import pe.edu.cibertec.apipatrimonioservice.repository.TransferenciaTitularidadRepository;
import pe.edu.cibertec.apipatrimonioservice.service.PatrimonioService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class PatrimonioServiceImpl implements PatrimonioService {

    private final SocioRepository socioRepository;
    private final PuestoRepository puestoRepository;
    private final ContratoOcupacionRepository contratoRepository;
    private final TransferenciaTitularidadRepository transferenciaRepository;
    private final TesoreriaClient tesoreriaClient;
    private final AuditoriaClient auditoriaClient;

    public PatrimonioServiceImpl(SocioRepository socioRepository,
                                 PuestoRepository puestoRepository,
                                 ContratoOcupacionRepository contratoRepository,
                                 TransferenciaTitularidadRepository transferenciaRepository,
                                 TesoreriaClient tesoreriaClient,
                                 AuditoriaClient auditoriaClient) {
        this.socioRepository = socioRepository;
        this.puestoRepository = puestoRepository;
        this.contratoRepository = contratoRepository;
        this.transferenciaRepository = transferenciaRepository;
        this.tesoreriaClient = tesoreriaClient;
        this.auditoriaClient = auditoriaClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponse> listarSocios() {
        return socioRepository.findAll().stream().map(PatrimonioMapper::toSocioResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponse> listarSociosActivosConContrato() {
        return contratoRepository.findByEstadoContrato(EstadoContrato.ACTIVO).stream()
                .map(contrato -> socioRepository.findById(contrato.getIdSocio()).orElse(null))
                .filter(Objects::nonNull)
                .filter(socio -> EstadoSocio.ACTIVO.equals(socio.getEstado()))
                .filter(socio -> !Boolean.TRUE.equals(socio.getEsAsociacion()))
                .distinct()
                .map(PatrimonioMapper::toSocioResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponse buscarSocio(Integer idSocio) {
        return PatrimonioMapper.toSocioResponse(obtenerSocio(idSocio));
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponse buscarSocioPorDni(String dni) {
        return PatrimonioMapper.toSocioResponse(socioRepository.findByDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado.")));
    }

    @Override
    @Transactional
    public SocioResponse registrarSocio(SocioRequest request) {
        validarDuplicadosSocioNuevo(request);
        Socio socio = Socio.builder()
                .dni(request.dni())
                .ruc(request.ruc())
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .telefono(request.telefono())
                .correo(request.correo())
                .direccion(request.direccion())
                .estado(request.estado() != null ? request.estado() : EstadoSocio.ACTIVO)
                .esAsociacion(Boolean.TRUE.equals(request.esAsociacion()))
                .build();
        return PatrimonioMapper.toSocioResponse(socioRepository.save(socio));
    }

    @Override
    @Transactional
    public SocioResponse actualizarSocio(Integer idSocio, SocioRequest request) {
        Socio socio = obtenerSocio(idSocio);
        validarDuplicadosSocioEdicion(socio, request);

        socio.setDni(request.dni());
        socio.setRuc(request.ruc());
        socio.setNombres(request.nombres());
        socio.setApellidos(request.apellidos());
        socio.setTelefono(request.telefono());
        socio.setCorreo(request.correo());
        socio.setDireccion(request.direccion());
        socio.setEstado(request.estado() != null ? request.estado() : socio.getEstado());
        socio.setEsAsociacion(Boolean.TRUE.equals(request.esAsociacion()));

        return PatrimonioMapper.toSocioResponse(socioRepository.save(socio));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PuestoResponse> listarPuestos() {
        return puestoRepository.findAll().stream().map(PatrimonioMapper::toPuestoResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PuestoResponse buscarPuesto(Integer idPuesto) {
        return PatrimonioMapper.toPuestoResponse(obtenerPuesto(idPuesto));
    }

    @Override
    @Transactional(readOnly = true)
    public PuestoResponse buscarPuestoPorCodigo(String codigoPuesto) {
        return PatrimonioMapper.toPuestoResponse(puestoRepository.findByCodigoPuesto(codigoPuesto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Puesto no encontrado.")));
    }

    @Override
    @Transactional
    public PuestoResponse registrarPuesto(PuestoRequest request) {
        if (puestoRepository.existsByCodigoPuesto(request.codigoPuesto())) {
            throw new ConflictoException("Ya existe un puesto con ese codigo.");
        }
        Puesto puesto = Puesto.builder()
                .codigoPuesto(request.codigoPuesto())
                .pabellon(request.pabellon())
                .medidas(request.medidas())
                .giro(request.giro())
                .estadoPuesto(request.estadoPuesto() != null ? request.estadoPuesto() : EstadoPuesto.LIBRE)
                .build();
        return PatrimonioMapper.toPuestoResponse(puestoRepository.save(puesto));
    }

    @Override
    @Transactional
    public PuestoResponse actualizarPuesto(Integer idPuesto, PuestoRequest request) {
        Puesto puesto = obtenerPuesto(idPuesto);
        puestoRepository.findByCodigoPuesto(request.codigoPuesto())
                .filter(actual -> !actual.getIdPuesto().equals(idPuesto))
                .ifPresent(actual -> {
                    throw new ConflictoException("Ya existe otro puesto con ese codigo.");
                });

        puesto.setCodigoPuesto(request.codigoPuesto());
        puesto.setPabellon(request.pabellon());
        puesto.setMedidas(request.medidas());
        puesto.setGiro(request.giro());
        puesto.setEstadoPuesto(request.estadoPuesto() != null ? request.estadoPuesto() : puesto.getEstadoPuesto());
        return PatrimonioMapper.toPuestoResponse(puestoRepository.save(puesto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratoResponse> listarContratosActivos() {
        return contratoRepository.findByEstadoContrato(EstadoContrato.ACTIVO).stream()
                .filter(this::contratoCobrable)
                .map(this::toContratoResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ContratoResponse buscarContrato(Integer idContrato) {
        return toContratoResponse(obtenerContrato(idContrato));
    }

    @Override
    @Transactional(readOnly = true)
    public ContratoResponse buscarContratoActivoPorPuesto(Integer idPuesto) {
        return toContratoResponse(obtenerContratoActivo(idPuesto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratoResponse> listarContratosActivosPorSocio(Integer idSocio) {
        obtenerSocio(idSocio);
        return contratoRepository.findByIdSocioAndEstadoContrato(idSocio, EstadoContrato.ACTIVO).stream()
                .map(this::toContratoResponse)
                .toList();
    }

    @Override
    @Transactional
    public ContratoResponse aperturarContrato(ContratoRequest request, Integer idUsuario) {
        Puesto puesto = obtenerPuesto(request.idPuesto());
        Socio socio = obtenerSocio(request.idSocio());

        if (!EstadoSocio.ACTIVO.equals(socio.getEstado())) {
            throw new ReglaNegocioException("El socio no se encuentra activo.");
        }
        if (EstadoPuesto.MANTENIMIENTO.equals(puesto.getEstadoPuesto())) {
            throw new ReglaNegocioException("No se puede aperturar contrato sobre un puesto en mantenimiento.");
        }

        contratoRepository.findFirstByIdPuestoAndEstadoContrato(puesto.getIdPuesto(), EstadoContrato.ACTIVO)
                .ifPresent(contrato -> cerrarContratoAdministrativoSiCorresponde(contrato, "Cierre automatico por nueva ocupacion"));

        ContratoOcupacion contrato = ContratoOcupacion.builder()
                .idPuesto(puesto.getIdPuesto())
                .idSocio(socio.getIdSocio())
                .fechaInicio(request.fechaInicio() != null ? request.fechaInicio() : LocalDate.now())
                .estadoContrato(EstadoContrato.ACTIVO)
                .idUsuarioRegistro(idUsuario)
                .build();
        ContratoOcupacion guardado = contratoRepository.save(contrato);

        puesto.setEstadoPuesto(Boolean.TRUE.equals(socio.getEsAsociacion()) ? EstadoPuesto.LIBRE : EstadoPuesto.OCUPADO);
        puestoRepository.save(puesto);

        registrarEvento("PATRIMONIO", "CONTRATO_APERTURADO", "contratos_ocupacion",
                guardado.getIdContrato(), idUsuario, "Contrato de ocupacion aperturado.");

        return toContratoResponse(guardado);
    }

    @Override
    @Transactional
    public ContratoResponse finalizarContrato(Integer idContrato, FinalizarContratoRequest request) {
        ContratoOcupacion contrato = obtenerContrato(idContrato);
        if (!EstadoContrato.ACTIVO.equals(contrato.getEstadoContrato())) {
            throw new ReglaNegocioException("Solo se puede finalizar un contrato activo.");
        }
        contrato.setEstadoContrato(EstadoContrato.FINALIZADO);
        contrato.setFechaFin(LocalDate.now());
        contrato.setMotivoCierre(request.motivoCierre());
        contratoRepository.save(contrato);

        Puesto puesto = obtenerPuesto(contrato.getIdPuesto());
        puesto.setEstadoPuesto(EstadoPuesto.LIBRE);
        puestoRepository.save(puesto);

        return toContratoResponse(contrato);
    }

    @Override
    @Transactional(readOnly = true)
    public SaneamientoResponse auditarSaneamiento(Integer idPuesto) {
        obtenerPuesto(idPuesto);
        DeudaResumenResponse resumen = tesoreriaClient.obtenerResumenDeudaPuesto(idPuesto);
        BigDecimal total = resumen.totalPendiente() != null ? resumen.totalPendiente() : BigDecimal.ZERO;
        int cantidad = resumen.cantidadCuotasPendientes() != null ? resumen.cantidadCuotasPendientes() : 0;
        boolean tieneDeuda = cantidad > 0 || total.compareTo(BigDecimal.ZERO) > 0;
        return new SaneamientoResponse(
                idPuesto,
                tieneDeuda,
                cantidad,
                total,
                tieneDeuda ? "El puesto mantiene deuda pendiente." : "El puesto se encuentra saneado."
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransferenciaResponse> listarTransferencias() {
        return transferenciaRepository.findAll().stream()
                .map(PatrimonioMapper::toTransferenciaResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TransferenciaResponse buscarTransferencia(Integer idTransferencia) {
        return PatrimonioMapper.toTransferenciaResponse(transferenciaRepository.findById(idTransferencia)
                .orElseThrow(() -> new RecursoNoEncontradoException("Transferencia no encontrada.")));
    }

    @Override
    @Transactional
    public TransferenciaResponse ejecutarTransferencia(TransferenciaRequest request, Integer idUsuario) {
        Puesto puesto = obtenerPuesto(request.idPuesto());
        Socio socioEntrante = obtenerSocio(request.idSocioEntrante());
        if (!EstadoSocio.ACTIVO.equals(socioEntrante.getEstado())) {
            throw new ReglaNegocioException("El socio entrante no se encuentra activo.");
        }

        ContratoOcupacion contratoSaliente = contratoRepository
                .findFirstByIdPuestoAndEstadoContrato(puesto.getIdPuesto(), EstadoContrato.ACTIVO)
                .orElse(null);
        DeudaResumenResponse deuda = tesoreriaClient.obtenerResumenDeudaPuesto(puesto.getIdPuesto());
        BigDecimal totalDeuda = deuda.totalPendiente() != null ? deuda.totalPendiente() : BigDecimal.ZERO;
        int cantidadDeudas = deuda.cantidadCuotasPendientes() != null ? deuda.cantidadCuotasPendientes() : 0;

        if (cantidadDeudas > 0 || totalDeuda.compareTo(BigDecimal.ZERO) > 0) {
            TransferenciaTitularidad bloqueada = construirTransferencia(request, contratoSaliente, idUsuario);
            bloqueada.setDeudaValidada(true);
            bloqueada.setMontoDeudaAsumida(totalDeuda);
            bloqueada.setEstadoTransferencia(EstadoTransferencia.BLOQUEADA);
            bloqueada.setObservacion("Transferencia bloqueada por deuda pendiente. " + texto(request.observacion()));
            return PatrimonioMapper.toTransferenciaResponse(transferenciaRepository.save(bloqueada));
        }

        if (contratoSaliente != null) {
            contratoSaliente.setEstadoContrato(EstadoContrato.FINALIZADO);
            contratoSaliente.setFechaFin(LocalDate.now());
            contratoSaliente.setMotivoCierre("Finalizado por transferencia de titularidad.");
            contratoRepository.save(contratoSaliente);
        }

        ContratoOcupacion contratoEntrante = ContratoOcupacion.builder()
                .idPuesto(puesto.getIdPuesto())
                .idSocio(socioEntrante.getIdSocio())
                .fechaInicio(request.fechaInicio() != null ? request.fechaInicio() : LocalDate.now())
                .estadoContrato(EstadoContrato.ACTIVO)
                .idUsuarioRegistro(idUsuario)
                .build();
        contratoEntrante = contratoRepository.save(contratoEntrante);

        puesto.setEstadoPuesto(Boolean.TRUE.equals(socioEntrante.getEsAsociacion()) ? EstadoPuesto.LIBRE : EstadoPuesto.OCUPADO);
        puestoRepository.save(puesto);

        TransferenciaTitularidad transferencia = construirTransferencia(request, contratoSaliente, idUsuario);
        transferencia.setDeudaValidada(true);
        transferencia.setMontoDeudaAsumida(BigDecimal.ZERO);
        transferencia.setIdContratoEntrante(contratoEntrante.getIdContrato());
        transferencia.setEstadoTransferencia(EstadoTransferencia.REGISTRADA);
        transferencia = transferenciaRepository.save(transferencia);

        registrarEvento("PATRIMONIO", "TRANSFERENCIA_REGISTRADA", "transferencias_titularidad",
                transferencia.getIdTransferencia(), idUsuario, "Transferencia de titularidad registrada.");

        return PatrimonioMapper.toTransferenciaResponse(transferencia);
    }

    private TransferenciaTitularidad construirTransferencia(TransferenciaRequest request,
                                                            ContratoOcupacion contratoSaliente,
                                                            Integer idUsuario) {
        return TransferenciaTitularidad.builder()
                .idPuesto(request.idPuesto())
                .idContratoSaliente(contratoSaliente != null ? contratoSaliente.getIdContrato() : null)
                .idSocioSaliente(contratoSaliente != null ? contratoSaliente.getIdSocio() : null)
                .idSocioEntrante(request.idSocioEntrante())
                .idUsuarioTramite(idUsuario)
                .costoTransferencia(request.costoTransferencia() != null ? request.costoTransferencia() : BigDecimal.ZERO)
                .deudaValidada(false)
                .asumeDeuda(Boolean.TRUE.equals(request.asumeDeuda()))
                .montoDeudaAsumida(BigDecimal.ZERO)
                .estadoTransferencia(EstadoTransferencia.REGISTRADA)
                .observacion(request.observacion())
                .build();
    }

    private void cerrarContratoAdministrativoSiCorresponde(ContratoOcupacion contrato, String motivo) {
        Socio socioActual = obtenerSocio(contrato.getIdSocio());
        if (!Boolean.TRUE.equals(socioActual.getEsAsociacion())) {
            throw new ReglaNegocioException("El puesto ya cuenta con un contrato activo.");
        }
        contrato.setEstadoContrato(EstadoContrato.FINALIZADO);
        contrato.setFechaFin(LocalDate.now());
        contrato.setMotivoCierre(motivo);
        contratoRepository.save(contrato);
    }

    private boolean contratoCobrable(ContratoOcupacion contrato) {
        Socio socio = obtenerSocio(contrato.getIdSocio());
        Puesto puesto = obtenerPuesto(contrato.getIdPuesto());
        return !Boolean.TRUE.equals(socio.getEsAsociacion())
                && EstadoSocio.ACTIVO.equals(socio.getEstado())
                && EstadoPuesto.OCUPADO.equals(puesto.getEstadoPuesto());
    }

    private ContratoResponse toContratoResponse(ContratoOcupacion contrato) {
        Puesto puesto = puestoRepository.findById(contrato.getIdPuesto()).orElse(null);
        Socio socio = socioRepository.findById(contrato.getIdSocio()).orElse(null);
        return PatrimonioMapper.toContratoResponse(contrato, puesto, socio);
    }

    private Socio obtenerSocio(Integer idSocio) {
        return socioRepository.findById(idSocio)
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado."));
    }

    private Puesto obtenerPuesto(Integer idPuesto) {
        return puestoRepository.findById(idPuesto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Puesto no encontrado."));
    }

    private ContratoOcupacion obtenerContrato(Integer idContrato) {
        return contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RecursoNoEncontradoException("Contrato no encontrado."));
    }

    private ContratoOcupacion obtenerContratoActivo(Integer idPuesto) {
        return contratoRepository.findFirstByIdPuestoAndEstadoContrato(idPuesto, EstadoContrato.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("El puesto no tiene contrato activo."));
    }

    private void validarDuplicadosSocioNuevo(SocioRequest request) {
        if (socioRepository.existsByDni(request.dni())) {
            throw new ConflictoException("Ya existe un socio con ese DNI.");
        }
        if (request.ruc() != null && socioRepository.existsByRuc(request.ruc())) {
            throw new ConflictoException("Ya existe un socio con ese RUC.");
        }
        if (request.correo() != null && socioRepository.existsByCorreo(request.correo())) {
            throw new ConflictoException("Ya existe un socio con ese correo.");
        }
    }

    private void validarDuplicadosSocioEdicion(Socio socio, SocioRequest request) {
        socioRepository.findAll().stream()
                .filter(actual -> !actual.getIdSocio().equals(socio.getIdSocio()))
                .filter(actual -> actual.getDni().equals(request.dni())
                        || (request.ruc() != null && request.ruc().equals(actual.getRuc()))
                        || (request.correo() != null && request.correo().equals(actual.getCorreo())))
                .findFirst()
                .ifPresent(actual -> {
                    throw new ConflictoException("DNI, RUC o correo ya registrado por otro socio.");
                });
    }

    private void registrarEvento(String modulo, String tipoEvento, String entidad, Integer idRegistro,
                                 Integer idUsuario, String descripcion) {
        try {
            auditoriaClient.registrarEvento(new AuditoriaEventoRequest(
                    modulo, tipoEvento, entidad, idRegistro, idUsuario, descripcion
            ));
        } catch (Exception ignored) {
            // La trazabilidad local de la operacion queda en el propio registro del dominio.
        }
    }

    private String texto(String value) {
        return value == null ? "" : value;
    }
}
