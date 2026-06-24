package pe.edu.cibertec.apipagosservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleDeudaDTO {
    private Integer idCuota;
    private Integer idServicio;
    private String nombreServicio;
    private Integer mes;
    private Integer anio;
    private Double monto;
}
