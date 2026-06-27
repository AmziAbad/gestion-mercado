package pe.edu.cibertec.apiauditoriareportesservice.entity;

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
@Table(name = "auditoria_anulaciones")
public class AuditoriaAnulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_anulacion", nullable = false)
    private TipoAnulacion tipoAnulacion;

    @Column(name = "id_registro_afectado", nullable = false)
    private Integer idRegistroAfectado;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "motivo_sustento", nullable = false)
    private String motivoSustento;

    @Column(name = "fecha_anulacion", nullable = false)
    private LocalDateTime fechaAnulacion;
}
