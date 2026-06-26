package pe.edu.cibertec.apiauditoriareportesservice.remote.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DeudaPendienteRemoteResponse(
        Integer idCuota,
        Integer idPuesto,
        Integer idContrato,
        Integer idConcepto,
        Integer periodoMes,
        Integer periodoAnio,
        BigDecimal montoTotal,
        LocalDate fechaVencimiento
) {
}
