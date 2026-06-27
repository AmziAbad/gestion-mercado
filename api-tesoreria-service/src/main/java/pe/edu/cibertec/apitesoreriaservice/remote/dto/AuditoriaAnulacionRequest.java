package pe.edu.cibertec.apitesoreriaservice.remote.dto;

public record AuditoriaAnulacionRequest(
        String tipoAnulacion,
        Integer idRegistroAfectado,
        Integer idUsuario,
        String motivoSustento
) {
}
