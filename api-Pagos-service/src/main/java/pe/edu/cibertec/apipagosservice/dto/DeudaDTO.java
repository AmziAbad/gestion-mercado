package pe.edu.cibertec.apipagosservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeudaDTO {
    private Integer idPuesto;
    private Integer idSocio;
    private Double totalDeuda;
    private Boolean tieneDeuda;
}
