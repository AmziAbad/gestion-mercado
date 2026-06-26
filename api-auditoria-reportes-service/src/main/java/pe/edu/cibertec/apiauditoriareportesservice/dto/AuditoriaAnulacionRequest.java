package pe.edu.cibertec.apiauditoriareportesservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.cibertec.apiauditoriareportesservice.entity.TipoAnulacion;

public record AuditoriaAnulacionRequest(
        @NotNull TipoAnulacion tipoAnulacion,
        @NotNull Integer idRegistroAfectado,
        @NotNull Integer idUsuario,
        @NotBlank String motivoSustento
) {
}
