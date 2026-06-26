package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CuotaAnulacionRequest(
        @NotBlank @Size(max = 255) String motivoAnulacion,
        Boolean generarReemplazo,
        @DecimalMin("0.01") BigDecimal montoReemplazo
) {
}
