package pe.edu.cibertec.apitransferenciaservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.apitransferenciaservice.dto.ActualizarTitularRequest;
import pe.edu.cibertec.apitransferenciaservice.dto.DeudaDTO;
import pe.edu.cibertec.apitransferenciaservice.dto.TransferenciaRequest;
import pe.edu.cibertec.apitransferenciaservice.entity.Transferencia;
import pe.edu.cibertec.apitransferenciaservice.remote.client.PagoClient;
import pe.edu.cibertec.apitransferenciaservice.remote.client.PuestoClient;
import pe.edu.cibertec.apitransferenciaservice.repository.TransferenciaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final PagoClient pagoClient;
    private final PuestoClient puestoClient;

    public TransferenciaService(TransferenciaRepository transferenciaRepository, PagoClient pagoClient, PuestoClient puestoClient) {
        this.transferenciaRepository = transferenciaRepository;
        this.pagoClient = pagoClient;
        this.puestoClient = puestoClient;
    }

    public List<Transferencia> listar() {
        return transferenciaRepository.findAll();
    }

    @Transactional
    public Transferencia registrar(TransferenciaRequest request) {
        // 1. Validar deudas del puesto llamando al microservicio de Pagos
        DeudaDTO deuda = pagoClient.obtenerDeudaPorPuesto(request.getIdPuesto());
        
        if (deuda != null && deuda.getTieneDeuda() && deuda.getTotalDeuda() > 0) {
            // Regla de negocio: el puesto no debe registrar deudas activas, salvo que se asuma.
            if (request.getAsumeDeuda() == null || !request.getAsumeDeuda()) {
                throw new RuntimeException("El puesto registra deudas activas por un total de S/ " + deuda.getTotalDeuda() + ". El nuevo socio debe aceptar explícitamente asumir la deuda pendiente para poder realizar el traspaso.");
            }
        }

        // 2. Actualizar el titular en el microservicio de Puestos
        ActualizarTitularRequest titularRequest = new ActualizarTitularRequest();
        titularRequest.setIdSocioActual(request.getIdSocioEntrante());
        titularRequest.setEstadoPuesto("OCUPADO");
        
        puestoClient.actualizarTitular(request.getIdPuesto(), titularRequest);

        // 3. Registrar la transferencia en base de datos
        Transferencia transferencia = Transferencia.builder()
                .idPuesto(request.getIdPuesto())
                .idSocioSaliente(request.getIdSocioSaliente())
                .idSocioEntrante(request.getIdSocioEntrante())
                .idUsuarioTramite(request.getIdUsuarioTramite() != null ? request.getIdUsuarioTramite() : 1)
                .costoTransferencia(request.getCostoTransferencia() != null ? request.getCostoTransferencia() : BigDecimal.ZERO)
                .fechaTramite(LocalDateTime.now())
                .observacion("Transferencia realizada" + (Boolean.TRUE.equals(request.getAsumeDeuda()) ? " asumiendo deuda anterior." : " sin deudas."))
                .build();

        return transferenciaRepository.save(transferencia);
    }
}
