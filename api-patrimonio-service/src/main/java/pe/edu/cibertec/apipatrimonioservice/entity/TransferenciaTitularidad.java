package pe.edu.cibertec.apipatrimonioservice.entity;

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
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transferencias_titularidad")
public class TransferenciaTitularidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transferencia")
    private Integer idTransferencia;

    @Column(name = "id_puesto", nullable = false)
    private Integer idPuesto;

    @Column(name = "id_contrato_saliente")
    private Integer idContratoSaliente;

    @Column(name = "id_socio_saliente")
    private Integer idSocioSaliente;

    @Column(name = "id_socio_entrante", nullable = false)
    private Integer idSocioEntrante;

    @Column(name = "id_contrato_entrante")
    private Integer idContratoEntrante;

    @Column(name = "id_usuario_tramite", nullable = false)
    private Integer idUsuarioTramite;

    @Column(name = "costo_transferencia", nullable = false)
    private BigDecimal costoTransferencia;

    @Column(name = "deuda_validada", nullable = false)
    private Boolean deudaValidada;

    @Column(name = "asume_deuda", nullable = false)
    private Boolean asumeDeuda;

    @Column(name = "monto_deuda_asumida", nullable = false)
    private BigDecimal montoDeudaAsumida;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_transferencia", nullable = false)
    private EstadoTransferencia estadoTransferencia;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_tramite", insertable = false, updatable = false)
    private LocalDateTime fechaTramite;
}
