package pe.edu.cibertec.apiusuariologinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.cibertec.apiusuariologinservice.entity.RolUsuario;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Integer idUsuario;
    private String username;
    private String nombreCompleto;
    private String dni;
    private String correo;
    private String telefono;
    private RolUsuario rol;
    private Boolean activo;
}
