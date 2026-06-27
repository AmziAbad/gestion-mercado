package pe.edu.cibertec.apitesoreriaservice.dto;

import pe.edu.cibertec.apitesoreriaservice.entity.EstadoCuota;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CuotaResponse(
        Integer idCuota,
        Integer idPuesto,
        Integer idContrato,
        Integer idConcepto,
        Integer periodoMes,
        Integer periodoAnio,
        BigDecimal montoTotal,
        EstadoCuota estadoCuota,
        LocalDateTime fechaGeneracion,
        LocalDate fechaVencimiento,
        Integer idUsuarioGeneracion,
        String motivoExoneracion,
        String motivoAnulacion,
        Integer idCuotaOrigen,
        Integer idCuotaReemplazo
) {
}
