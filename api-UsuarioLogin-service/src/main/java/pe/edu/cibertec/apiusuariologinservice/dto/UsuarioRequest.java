package pe.edu.cibertec.apiusuariologinservice.dto;

import lombok.Data;
import pe.edu.cibertec.apiusuariologinservice.entity.RolUsuario;

@Data
public class UsuarioRequest {
    private String username;
    private String password;
    private String nombreCompleto;
    private String dni;
    private String correo;
    private String telefono;
    private RolUsuario rol;
    private Boolean activo;
}
