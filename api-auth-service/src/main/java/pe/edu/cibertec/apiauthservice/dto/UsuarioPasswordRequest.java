package pe.edu.cibertec.apiauthservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioPasswordRequest(
        @NotBlank @Size(min = 6, max = 80) String password
) {
}
