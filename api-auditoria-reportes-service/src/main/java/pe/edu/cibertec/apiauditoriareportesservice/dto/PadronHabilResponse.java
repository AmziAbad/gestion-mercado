package pe.edu.cibertec.apiauditoriareportesservice.dto;

public record PadronHabilResponse(
        Integer idSocio,
        String dni,
        String nombres,
        String apellidos,
        String correo,
        String telefono
) {
}
