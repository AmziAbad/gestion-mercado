package pe.edu.cibertec.apitransferenciaservice.dto;

import lombok.Data;

@Data
public class SocioDTO {
    private Integer idSocio;
    private String dni;
    private String nombre;
    private String apellido;
    private Boolean activo;
}
