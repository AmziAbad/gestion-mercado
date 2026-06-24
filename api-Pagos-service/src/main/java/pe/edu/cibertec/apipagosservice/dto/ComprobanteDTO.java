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
public class ComprobanteDTO {
    private String titulo;
    private Integer idCuota;
    private String numeroOperacion;
    private String numeroComprobante;
    private LocalDateTime fechaEmision;
    private Double montoPagado;
    private MetodoPago metodoPago;
    private Integer idPuesto;
    private String numeroPuesto;
    private String estadoPuesto;
    private Integer idServicio;
    private String nombreServicio;
    private String detallePuesto;
    private String detalleServicio;
    private String periodo;
    private String mensaje;
}
