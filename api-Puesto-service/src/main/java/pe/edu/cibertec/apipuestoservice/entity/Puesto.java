package pe.edu.cibertec.apipuestoservice.entity;

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
@Table(name = "puestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Puesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puesto")
    private Integer idPuesto;

    @NotBlank
    @Column(name = "numero_puesto", nullable = false, unique = true, length = 10)
    private String numeroPuesto;

    @NotBlank
    @Column(nullable = false, length = 30)
    private String pabellon;

    @Builder.Default
    @Column(length = 20)
    private String medidas = "2x2m";

    @NotNull
    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_puesto")
    private EstadoPuesto estadoPuesto = EstadoPuesto.VACANTE;

    @Column(name = "id_socio_actual")
    private Integer idSocioActual;

    @Column(name = "id_giro")
    private Integer idGiro;
}
