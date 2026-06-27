package pe.edu.cibertec.apitesoreriaservice.remote.dto;

public record AuditoriaEventoRequest(
        String modulo,
        String tipoEvento,
        String entidadAfectada,
        Integer idRegistroAfectado,
        Integer idUsuario,
        String descripcion
) {
}
