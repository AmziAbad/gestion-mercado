package pe.edu.cibertec.apipatrimonioservice.remote.dto;

import java.math.BigDecimal;

public record DeudaResumenResponse(
        Integer idPuesto,
        Integer cantidadCuotasPendientes,
        BigDecimal totalPendiente
) {
}
