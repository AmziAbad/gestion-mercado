package pe.edu.cibertec.apipatrimonioservice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ContratoRequest(
        @NotNull Integer idPuesto,
        @NotNull Integer idSocio,
        LocalDate fechaInicio
) {
}
