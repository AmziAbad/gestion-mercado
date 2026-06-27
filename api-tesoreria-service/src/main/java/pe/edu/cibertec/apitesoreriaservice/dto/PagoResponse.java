package pe.edu.cibertec.apitesoreriaservice.dto;

import pe.edu.cibertec.apitesoreriaservice.entity.EstadoPago;
import pe.edu.cibertec.apitesoreriaservice.entity.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoResponse(
        Integer idPago,
        Integer idCuota,
        Integer idTurno,
        Integer idUsuarioCobro,
        MetodoPago metodoPago,
        String numeroOperacion,
        BigDecimal montoPagado,
        LocalDateTime fechaPago,
        EstadoPago estadoPago,
        String motivoExtorno,
        LocalDateTime fechaExtorno,
        Integer idUsuarioExtorno,
        ComprobanteResponse comprobante
) {
}
