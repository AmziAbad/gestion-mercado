package pe.edu.cibertec.apitesoreriaservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DeudaPendienteReporteResponse(
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
