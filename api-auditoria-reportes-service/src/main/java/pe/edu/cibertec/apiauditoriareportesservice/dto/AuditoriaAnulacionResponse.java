package pe.edu.cibertec.apiauditoriareportesservice.dto;

import pe.edu.cibertec.apiauditoriareportesservice.entity.TipoAnulacion;

import java.time.LocalDateTime;

public record AuditoriaAnulacionResponse(
        Integer idAuditoria,
        TipoAnulacion tipoAnulacion,
        Integer idRegistroAfectado,
        Integer idUsuario,
        String motivoSustento,
        LocalDateTime fechaAnulacion
) {
}
