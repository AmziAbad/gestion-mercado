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
public class DeudorDTO {
    private Integer idPuesto;
    private String numeroPuesto;
    private String pabellon;
    private Integer idSocio;
    private String nombreSocio;
    private Integer totalCuotasPendientes;
    private Double totalDeuda;
    private List<DetalleDeudaDTO> cuotas;
}
