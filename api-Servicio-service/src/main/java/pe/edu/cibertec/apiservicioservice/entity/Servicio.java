package pe.edu.cibertec.apiservicioservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(length = 50)
    private String nombre;

    @NotBlank
    @Column(name = "nombre_servicio", nullable = false, length = 50)
    private String nombreServicio;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobro", nullable = false)
    private TipoCobro tipoCobro;

    @Column(name = "costo_total_externo", precision = 10, scale = 2)
    private BigDecimal costoTotalExterno;

    @Column(name = "monto_fijo_puesto", precision = 10, scale = 2)
    private BigDecimal montoFijoPuesto;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
}
