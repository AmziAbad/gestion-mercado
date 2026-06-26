package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CuotaMasivaRequest(
        @NotNull Integer idConcepto,
        @NotNull @Min(1) @Max(12) Integer periodoMes,
        @NotNull @Min(2020) Integer periodoAnio,
        LocalDate fechaVencimiento
) {
}
