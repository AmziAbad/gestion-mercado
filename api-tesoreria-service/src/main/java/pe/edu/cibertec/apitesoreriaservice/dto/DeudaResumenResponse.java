package pe.edu.cibertec.apitesoreriaservice.dto;

import java.math.BigDecimal;

public record DeudaResumenResponse(
        Integer idPuesto,
        Integer cantidadCuotasPendientes,
        BigDecimal totalPendiente
) {
}
