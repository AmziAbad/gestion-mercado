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
@Table(name = "conceptos_cobro")
public class ConceptoCobro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_concepto")
    private Integer idConcepto;

    @Column(name = "nombre_concepto", nullable = false, length = 100)
    private String nombreConcepto;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobro", nullable = false)
    private TipoCobro tipoCobro;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodicidad", nullable = false)
    private Periodicidad periodicidad;

    @Column(name = "monto_fijo")
    private BigDecimal montoFijo;

    @Column(name = "costo_total_prorrateo")
    private BigDecimal costoTotalProrrateo;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}
