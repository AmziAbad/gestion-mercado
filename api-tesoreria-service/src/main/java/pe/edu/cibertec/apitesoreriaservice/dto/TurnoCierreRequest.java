package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TurnoCierreRequest(
        @NotNull @DecimalMin(value = "0.00") BigDecimal montoRecaudado,
        @Size(max = 255) String observacionCierre
) {
}
