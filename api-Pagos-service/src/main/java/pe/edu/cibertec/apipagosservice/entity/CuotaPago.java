package pe.edu.cibertec.apipagosservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cuotas_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuotaPago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuota;
    @Column(name = "id_puesto", nullable = false)
    private Integer idPuesto;
    @Column(name = "id_servicio", nullable = false)
    private Integer idServicio;
    private Integer mes;
    private Integer anio;
    private Double monto;
    @Enumerated(EnumType.STRING)
    private EstadoPago estado = EstadoPago.PENDIENTE;
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;

    @Column(name = "numero_operacion")
    private String numeroOperacion;

    @Column(name = "numero_comprobante")
    private String numeroComprobante;

    @Column(name = "motivo_exoneracion")
    private String motivoExoneracion;

    @Column(name = "fecha_exoneracion")
    private LocalDateTime fechaExoneracion;

    @Column(name = "motivo_anulacion")
    private String motivoAnulacion;

    @Column(name = "fecha_anulacion")
    private LocalDateTime fechaAnulacion;

    @Column(name = "motivo_anulacion_pago")
    private String motivoAnulacionPago;

    @Column(name = "fecha_anulacion_pago")
    private LocalDateTime fechaAnulacionPago;

    @Column(name = "id_cuota_origen")
    private Integer idCuotaOrigen;

    @Column(name = "id_cuota_reemplazo")
    private Integer idCuotaReemplazo;

}
