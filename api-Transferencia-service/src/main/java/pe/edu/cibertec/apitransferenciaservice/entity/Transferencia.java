package pe.edu.cibertec.apitransferenciaservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transferencia")
    private Integer idTransferencia;

    @Column(name = "id_puesto", nullable = false)
    private Integer idPuesto;

    @Column(name = "id_socio_saliente")
    private Integer idSocioSaliente;

    @Column(name = "id_socio_entrante", nullable = false)
    private Integer idSocioEntrante;

    @Column(name = "id_usuario_tramite", nullable = false)
    private Integer idUsuarioTramite;

    @Column(name = "costo_transferencia")
    private BigDecimal costoTransferencia;

    @Column(name = "fecha_tramite")
    private LocalDateTime fechaTramite;

    @Column(name = "asume_deuda")
    private Boolean asumeDeuda;

    @Column(name = "monto_deuda_asumida")
    private BigDecimal montoDeudaAsumida;

    @Column(name = "observacion")
    private String observacion;
}
