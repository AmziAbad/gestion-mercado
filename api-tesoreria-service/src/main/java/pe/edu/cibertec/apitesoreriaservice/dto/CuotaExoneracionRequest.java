package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CuotaExoneracionRequest(
        @NotBlank @Size(max = 255) String motivoExoneracion
) {
}
