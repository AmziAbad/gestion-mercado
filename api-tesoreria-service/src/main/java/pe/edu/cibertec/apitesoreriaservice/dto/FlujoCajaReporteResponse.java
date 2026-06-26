package pe.edu.cibertec.apitesoreriaservice.dto;

import pe.edu.cibertec.apitesoreriaservice.entity.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlujoCajaReporteResponse(
        Integer idPago,
        Integer idTurno,
        Integer idCuota,
        Integer idUsuarioCobro,
        MetodoPago metodoPago,
        BigDecimal montoPagado,
        LocalDateTime fechaPago
) {
}
