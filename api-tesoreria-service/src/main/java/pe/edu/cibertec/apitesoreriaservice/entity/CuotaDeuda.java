package pe.edu.cibertec.apitesoreriaservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuotas_deuda")
public class CuotaDeuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuota")
    private Integer idCuota;

    @Column(name = "id_puesto", nullable = false)
    private Integer idPuesto;

    @Column(name = "id_contrato")
    private Integer idContrato;

    @Column(name = "id_concepto", nullable = false)
    private Integer idConcepto;

    @Column(name = "periodo_mes")
    private Integer periodoMes;

    @Column(name = "periodo_anio")
    private Integer periodoAnio;

    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuota", nullable = false)
    private EstadoCuota estadoCuota;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "id_usuario_generacion", nullable = false)
    private Integer idUsuarioGeneracion;

    @Column(name = "motivo_exoneracion", length = 255)
    private String motivoExoneracion;

    @Column(name = "fecha_exoneracion")
    private LocalDateTime fechaExoneracion;

    @Column(name = "id_usuario_exoneracion")
    private Integer idUsuarioExoneracion;

    @Column(name = "motivo_anulacion", length = 255)
    private String motivoAnulacion;

    @Column(name = "fecha_anulacion")
    private LocalDateTime fechaAnulacion;

    @Column(name = "id_usuario_anulacion")
    private Integer idUsuarioAnulacion;

    @Column(name = "id_cuota_origen")
    private Integer idCuotaOrigen;

    @Column(name = "id_cuota_reemplazo")
    private Integer idCuotaReemplazo;
}
