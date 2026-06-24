package pe.edu.cibertec.apipagosservice.dto;

import lombok.Data;

@Data
public class SocioDTO {
    private Integer idSocio;
    private String dni;
    private String ruc;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private Boolean activo;
}
