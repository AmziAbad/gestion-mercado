package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CuotaEspecificaRequest(
        @NotNull Integer idPuesto,
        Integer idContrato,
        @NotNull Integer idConcepto,
        @DecimalMin("0.01") BigDecimal montoTotal,
        @Min(1) @Max(12) Integer periodoMes,
        @Min(2020) Integer periodoAnio,
        LocalDate fechaVencimiento
) {
}
