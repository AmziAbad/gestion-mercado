package pe.edu.cibertec.apiauthservice.dto;

import pe.edu.cibertec.apiauthservice.entity.RolUsuario;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Integer idUsuario,
        String username,
        String nombreCompleto,
        String dni,
        String correo,
        String telefono,
        RolUsuario rol,
        Boolean activo,
        LocalDateTime fechaRegistro
) {
}
