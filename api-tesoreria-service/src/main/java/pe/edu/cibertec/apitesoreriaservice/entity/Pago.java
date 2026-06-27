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
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "id_cuota", nullable = false)
    private Integer idCuota;

    @Column(name = "id_turno", nullable = false)
    private Integer idTurno;

    @Column(name = "id_usuario_cobro", nullable = false)
    private Integer idUsuarioCobro;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "numero_operacion", length = 80)
    private String numeroOperacion;

    @Column(name = "monto_pagado", nullable = false)
    private BigDecimal montoPagado;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPago estadoPago;

    @Column(name = "motivo_extorno", length = 255)
    private String motivoExtorno;

    @Column(name = "fecha_extorno")
    private LocalDateTime fechaExtorno;

    @Column(name = "id_usuario_extorno")
    private Integer idUsuarioExtorno;
}
