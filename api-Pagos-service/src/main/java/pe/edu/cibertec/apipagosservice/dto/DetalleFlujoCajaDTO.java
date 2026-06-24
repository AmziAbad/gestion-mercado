package pe.edu.cibertec.apipagosservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.cibertec.apipagosservice.entity.MetodoPago;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleFlujoCajaDTO {
    private Integer idCuota;
    private Integer idPuesto;
    private String numeroPuesto;
    private Integer idSocio;
    private String nombreSocio;
    private Integer idServicio;
    private String nombreServicio;
    private Double monto;
    private MetodoPago metodoPago;
    private String numeroOperacion;
    private String numeroComprobante;
    private LocalDateTime fechaPago;
}
