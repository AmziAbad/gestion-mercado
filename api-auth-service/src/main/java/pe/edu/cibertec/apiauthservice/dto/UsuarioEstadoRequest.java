package pe.edu.cibertec.apiauthservice.dto;

import jakarta.validation.constraints.NotNull;

public record UsuarioEstadoRequest(
        @NotNull Boolean activo
) {
}
