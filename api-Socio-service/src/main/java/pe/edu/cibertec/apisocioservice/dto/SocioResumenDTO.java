package pe.edu.cibertec.apisocioservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocioResumenDTO {
    private Integer idSocio;
    private String dni;
    private String nombreCompleto;
    private long cantidadPuestos;
    private double totalDeuda;
    private String estado;
}
