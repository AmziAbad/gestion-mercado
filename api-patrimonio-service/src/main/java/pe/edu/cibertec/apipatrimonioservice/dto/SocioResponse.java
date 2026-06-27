package pe.edu.cibertec.apipatrimonioservice.dto;

import pe.edu.cibertec.apipatrimonioservice.entity.EstadoSocio;

import java.time.LocalDateTime;

public record SocioResponse(
        Integer idSocio,
        String dni,
        String ruc,
        String nombres,
        String apellidos,
        String telefono,
        String correo,
        String direccion,
        EstadoSocio estado,
        Boolean esAsociacion,
        LocalDateTime fechaRegistro
) {
}
