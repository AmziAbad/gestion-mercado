package pe.edu.cibertec.apitesoreriaservice.dto;

import java.math.BigDecimal;
import java.util.List;

public record EstadoCuentaResponse(
        Integer idPuesto,
        Integer idSocio,
        Integer cantidadCuotasPendientes,
        BigDecimal totalPendiente,
        List<CuotaResponse> cuotas
) {
}
