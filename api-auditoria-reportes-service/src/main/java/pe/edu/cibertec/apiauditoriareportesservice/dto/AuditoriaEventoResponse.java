package pe.edu.cibertec.apiauditoriareportesservice.dto;

import java.time.LocalDateTime;

public record AuditoriaEventoResponse(
        Integer idEvento,
        String modulo,
        String tipoEvento,
        String entidadAfectada,
        Integer idRegistroAfectado,
        Integer idUsuario,
        String descripcion,
        LocalDateTime fechaEvento
) {
}
