package pe.edu.cibertec.apitesoreriaservice.dto;

import pe.edu.cibertec.apitesoreriaservice.entity.EstadoComprobante;
import pe.edu.cibertec.apitesoreriaservice.entity.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComprobanteResponse(
        Integer idComprobante,
        Integer idPago,
        Integer idCuota,
        String numeroComprobante,
        LocalDateTime fechaEmision,
        BigDecimal montoTotal,
        MetodoPago metodoPago,
        EstadoComprobante estadoComprobante
) {
}
