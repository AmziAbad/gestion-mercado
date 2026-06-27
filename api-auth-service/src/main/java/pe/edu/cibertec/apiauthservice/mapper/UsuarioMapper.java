package pe.edu.cibertec.apiauthservice.mapper;

import pe.edu.cibertec.apiauthservice.dto.UsuarioResponse;
import pe.edu.cibertec.apiauthservice.entity.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getNombreCompleto(),
                usuario.getDni(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getFechaRegistro()
        );
    }
}
