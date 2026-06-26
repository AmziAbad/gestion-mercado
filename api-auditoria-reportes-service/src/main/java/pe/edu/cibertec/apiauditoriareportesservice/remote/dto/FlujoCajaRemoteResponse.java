package pe.edu.cibertec.apiauditoriareportesservice.remote.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlujoCajaRemoteResponse(
        Integer idPago,
        Integer idTurno,
        Integer idCuota,
        Integer idUsuarioCobro,
        String metodoPago,
        BigDecimal montoPagado,
        LocalDateTime fechaPago
) {
}
