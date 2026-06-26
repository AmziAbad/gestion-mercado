package pe.edu.cibertec.apipatrimonioservice.remote.dto;

public record AuditoriaEventoRequest(
        String modulo,
        String tipoEvento,
        String entidadAfectada,
        Integer idRegistroAfectado,
        Integer idUsuario,
        String descripcion
) {
}
