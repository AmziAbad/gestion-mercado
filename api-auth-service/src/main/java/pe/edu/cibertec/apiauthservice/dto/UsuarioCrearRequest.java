package pe.edu.cibertec.apiauthservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.edu.cibertec.apiauthservice.entity.RolUsuario;

public record UsuarioCrearRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(min = 6, max = 80) String password,
        @NotBlank @Size(max = 100) String nombreCompleto,
        @NotBlank @Pattern(regexp = "\\d{8}") String dni,
        @Email @Size(max = 150) String correo,
        @Size(max = 20) String telefono,
        @NotNull RolUsuario rol
) {
}
