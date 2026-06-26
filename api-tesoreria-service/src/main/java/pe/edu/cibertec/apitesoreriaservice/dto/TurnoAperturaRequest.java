package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TurnoAperturaRequest(
        @DecimalMin("0.00") BigDecimal montoInicial,
        @Size(max = 255) String observacionApertura
) {
}
