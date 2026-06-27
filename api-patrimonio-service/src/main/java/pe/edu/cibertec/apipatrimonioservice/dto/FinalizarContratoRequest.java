package pe.edu.cibertec.apipatrimonioservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FinalizarContratoRequest(
        @NotBlank @Size(max = 255) String motivoCierre
) {
}
