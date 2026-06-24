package pe.edu.cibertec.apisocioservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "socios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    private Integer idSocio;
    @Column(nullable = false,unique = true, length = 8)
    private String dni;
    @Column(unique = true, length = 11)
    private String ruc;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String direccion;
    @Column(name = "estado_solvencia")
    private Boolean estadoSolvencia = true;
    private Boolean activo = false;
    @Column(name = "es_asociacion")
    private Boolean esAsociacion = false;
}
