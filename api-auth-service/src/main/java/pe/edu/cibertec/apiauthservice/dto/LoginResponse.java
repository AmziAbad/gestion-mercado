package pe.edu.cibertec.apiauthservice.dto;

public record LoginResponse(
        Integer idUsuario,
        String username,
        String nombreCompleto,
        String rol,
        String token,
        Long expirationSeconds
) {
}
