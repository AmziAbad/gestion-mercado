package pe.edu.cibertec.apiauditoriareportesservice.remote.dto;

public record SocioRemoteResponse(
        Integer idSocio,
        String dni,
        String ruc,
        String nombres,
        String apellidos,
        String telefono,
        String correo,
        String direccion,
        String estado,
        Boolean esAsociacion
) {
}
