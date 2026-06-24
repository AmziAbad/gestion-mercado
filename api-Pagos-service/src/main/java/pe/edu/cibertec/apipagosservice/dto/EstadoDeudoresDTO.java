package pe.edu.cibertec.apipagosservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDeudoresDTO {
    private Integer totalPuestosConDeuda;
    private Integer totalCuotasPendientes;
    private Double totalDeuda;
    private List<DeudorDTO> deudores;
}
