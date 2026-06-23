package pe.edu.cibertec.apitransferenciaservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.apitransferenciaservice.dto.ActualizarTitularRequest;
import pe.edu.cibertec.apitransferenciaservice.dto.DeudaDTO;
import pe.edu.cibertec.apitransferenciaservice.dto.PuestoDTO;
import pe.edu.cibertec.apitransferenciaservice.dto.TransferenciaRequest;
import pe.edu.cibertec.apitransferenciaservice.entity.Transferencia;
import pe.edu.cibertec.apitransferenciaservice.remote.client.PagoClient;
import pe.edu.cibertec.apitransferenciaservice.remote.client.PuestoClient;
import pe.edu.cibertec.apitransferenciaservice.remote.client.SocioClient;
import pe.edu.cibertec.apitransferenciaservice.repository.TransferenciaRepository;
import pe.edu.cibertec.apitransferenciaservice.service.TransferenciaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferenciaServiceImpl implements TransferenciaService {
    private final TransferenciaRepository transferenciaRepository;
    private final PagoClient pagoClient;
    private final PuestoClient puestoClient;
    private final SocioClient socioClient;

    @Override
    public List<Transferencia> listar() {
        return transferenciaRepository.findAll();
    }

    @Override
    public Optional<Transferencia> obtener(Integer id) {
        return transferenciaRepository.findById(id);
    }

    @Override
    public List<Transferencia> listarPorPuesto(Integer idPuesto) {
        if (idPuesto == null) {
            throw new RuntimeException("Debe indicar el puesto.");
        }
        return transferenciaRepository.findByIdPuesto(idPuesto);
    }

    @Override
    @Transactional
    public Transferencia registrar(TransferenciaRequest request) {
        validarRequest(request);

        PuestoDTO puesto = obtenerPuesto(request.getIdPuesto());
        validarSocioEntrante(request.getIdSocioEntrante());
        validarSocioSaliente(request, puesto);
        validarDeuda(request);

        Transferencia transferencia = transferenciaRepository.saveAndFlush(construirTransferencia(request));
        actualizarTitular(request);
        
        // Módulo C -> Módulo A: Verificamos si el socio saliente se quedó sin puestos tras el traspaso
        if (request.getIdSocioSaliente() != null) {
            try {
                socioClient.verificarActividad(request.getIdSocioSaliente());
            } catch (Exception e) {
                // Si la verificación falla, no detenemos el traspaso (compensación simple)
                System.out.println("No se pudo verificar la actividad del socio saliente: " + e.getMessage());
            }
        }
        
        return transferencia;
    }

    private void validarRequest(TransferenciaRequest request) {
        if (request == null) {
            throw new RuntimeException("Debe enviar los datos de la transferencia.");
        }
        if (request.getIdPuesto() == null) {
            throw new RuntimeException("Debe indicar el puesto a transferir.");
        }
        if (request.getIdSocioEntrante() == null) {
            throw new RuntimeException("Debe indicar el socio entrante.");
        }
        if (request.getIdUsuarioTramite() == null) {
            throw new RuntimeException("Debe indicar el usuario que registra el tramite.");
        }
        if (request.getIdSocioSaliente() != null
                && request.getIdSocioSaliente().equals(request.getIdSocioEntrante())) {
            throw new RuntimeException("El socio saliente y el socio entrante no pueden ser el mismo.");
        }
    }

    private PuestoDTO obtenerPuesto(Integer idPuesto) {
        try {
            return puestoClient.obtener(idPuesto);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El puesto indicado no existe.");
        }
    }

    private void validarSocioEntrante(Integer idSocioEntrante) {
        try {
            socioClient.obtener(idSocioEntrante);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El socio entrante no existe.");
        }
    }

    private void validarSocioSaliente(TransferenciaRequest request, PuestoDTO puesto) {
        Integer socioActual = puesto.getIdSocioActual();
        Integer socioSaliente = request.getIdSocioSaliente();

        if (socioActual == null && socioSaliente != null) {
            throw new RuntimeException("El puesto no tiene socio saliente registrado.");
        }
        if (socioActual != null && !socioActual.equals(socioSaliente)) {
            throw new RuntimeException("El socio saliente no coincide con el titular actual del puesto.");
        }
    }

    private void validarDeuda(TransferenciaRequest request) {
        DeudaDTO deuda = pagoClient.obtenerDeudaPorPuesto(request.getIdPuesto());
        double totalDeuda = deuda != null && deuda.getTotalDeuda() != null
                ? deuda.getTotalDeuda()
                : 0.0;

        if (deuda != null && Boolean.TRUE.equals(deuda.getTieneDeuda()) && totalDeuda > 0
                && !Boolean.TRUE.equals(request.getAsumeDeuda())) {
            throw new RuntimeException("El puesto registra deudas activas por un total de S/ "
                    + totalDeuda
                    + ". El nuevo socio debe aceptar asumir la deuda pendiente para realizar el traspaso.");
        }
    }

    private Transferencia construirTransferencia(TransferenciaRequest request) {
        return Transferencia.builder()
                .idPuesto(request.getIdPuesto())
                .idSocioSaliente(request.getIdSocioSaliente())
                .idSocioEntrante(request.getIdSocioEntrante())
                .idUsuarioTramite(request.getIdUsuarioTramite())
                .costoTransferencia(request.getCostoTransferencia() != null
                        ? request.getCostoTransferencia()
                        : BigDecimal.ZERO)
                .fechaTramite(LocalDateTime.now())
                .observacion("Transferencia realizada"
                        + (Boolean.TRUE.equals(request.getAsumeDeuda())
                        ? " asumiendo deuda anterior."
                        : " sin deudas."))
                .build();
    }

    private void actualizarTitular(TransferenciaRequest request) {
        ActualizarTitularRequest titularRequest = new ActualizarTitularRequest();
        titularRequest.setIdSocioActual(request.getIdSocioEntrante());
        titularRequest.setEstadoPuesto("OCUPADO");

        puestoClient.actualizarTitular(request.getIdPuesto(), titularRequest);
    }
}
