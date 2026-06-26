package pe.edu.cibertec.apiauditoriareportesservice.mapper;

import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaAnulacionResponse;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaEventoResponse;
import pe.edu.cibertec.apiauditoriareportesservice.entity.AuditoriaAnulacion;
import pe.edu.cibertec.apiauditoriareportesservice.entity.AuditoriaEvento;

public final class AuditoriaMapper {

    private AuditoriaMapper() {
    }

    public static AuditoriaEventoResponse toEventoResponse(AuditoriaEvento evento) {
        return new AuditoriaEventoResponse(
                evento.getIdEvento(),
                evento.getModulo(),
                evento.getTipoEvento(),
                evento.getEntidadAfectada(),
                evento.getIdRegistroAfectado(),
                evento.getIdUsuario(),
                evento.getDescripcion(),
                evento.getFechaEvento()
        );
    }

    public static AuditoriaAnulacionResponse toAnulacionResponse(AuditoriaAnulacion anulacion) {
        return new AuditoriaAnulacionResponse(
                anulacion.getIdAuditoria(),
                anulacion.getTipoAnulacion(),
                anulacion.getIdRegistroAfectado(),
                anulacion.getIdUsuario(),
                anulacion.getMotivoSustento(),
                anulacion.getFechaAnulacion()
        );
    }
}
