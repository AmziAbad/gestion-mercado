package pe.edu.cibertec.apipatrimonioservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.edu.cibertec.apipatrimonioservice.entity.EstadoSocio;

public record SocioRequest(
        @NotBlank @Pattern(regexp = "\\d{8}") String dni,
        @Pattern(regexp = "\\d{11}") String ruc,
        @NotBlank @Size(max = 100) String nombres,
        @NotBlank @Size(max = 100) String apellidos,
        @Size(max = 20) String telefono,
        @Email @Size(max = 150) String correo,
        @Size(max = 180) String direccion,
        EstadoSocio estado,
        Boolean esAsociacion
) {
}
