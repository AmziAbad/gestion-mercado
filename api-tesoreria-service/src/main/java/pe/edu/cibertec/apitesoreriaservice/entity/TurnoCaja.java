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
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "turnos_caja")
public class TurnoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Integer idTurno;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "monto_inicial", nullable = false)
    private BigDecimal montoInicial;

    @Column(name = "monto_recaudado", nullable = false)
    private BigDecimal montoRecaudado;

    @Column(name = "monto_esperado", nullable = false)
    private BigDecimal montoEsperado;

    @Column(name = "diferencia", nullable = false)
    private BigDecimal diferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_turno", nullable = false)
    private EstadoTurno estadoTurno;

    @Column(name = "observacion_apertura", length = 255)
    private String observacionApertura;

    @Column(name = "observacion_cierre", length = 255)
    private String observacionCierre;
}
