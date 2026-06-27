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

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "puestos")
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puesto")
    private Integer idPuesto;

    @Column(name = "codigo_puesto", nullable = false, unique = true, length = 20)
    private String codigoPuesto;

    @Column(name = "pabellon", nullable = false, length = 50)
    private String pabellon;

    @Column(name = "medidas", length = 50)
    private String medidas;

    @Column(name = "giro", length = 80)
    private String giro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_puesto", nullable = false)
    private EstadoPuesto estadoPuesto;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}
