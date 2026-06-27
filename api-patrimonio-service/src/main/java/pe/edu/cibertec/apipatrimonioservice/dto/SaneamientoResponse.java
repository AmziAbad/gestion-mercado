package pe.edu.cibertec.apipatrimonioservice.dto;

import java.math.BigDecimal;

public record SaneamientoResponse(
        Integer idPuesto,
        Boolean tieneDeuda,
        Integer cantidadCuotasPendientes,
        BigDecimal totalPendiente,
        String mensaje
) {
}
