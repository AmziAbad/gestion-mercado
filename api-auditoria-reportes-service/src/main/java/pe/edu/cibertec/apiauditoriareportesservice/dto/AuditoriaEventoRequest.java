package pe.edu.cibertec.apiauditoriareportesservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuditoriaEventoRequest(
        @NotBlank @Size(max = 50) String modulo,
        @NotBlank @Size(max = 80) String tipoEvento,
        @NotBlank @Size(max = 80) String entidadAfectada,
        @NotNull Integer idRegistroAfectado,
        @NotNull Integer idUsuario,
        @NotBlank String descripcion
) {
}
