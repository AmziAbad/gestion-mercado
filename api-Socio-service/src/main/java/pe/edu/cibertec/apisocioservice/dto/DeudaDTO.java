package pe.edu.cibertec.apisocioservice.dto;

import lombok.Data;

@Data
public class DeudaDTO {
    private Integer idSocio;
    private Double totalDeuda;
    private Boolean tieneDeuda;
}
