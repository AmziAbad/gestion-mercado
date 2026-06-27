package pe.edu.cibertec.apiauditoriareportesservice.remote.dto;

import java.math.BigDecimal;

public record EstadoCuentaRemoteResponse(
        Integer idPuesto,
        Integer idSocio,
        Integer cantidadCuotasPendientes,
        BigDecimal totalPendiente
) {
}
