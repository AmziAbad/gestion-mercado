package pe.edu.cibertec.apipatrimonioservice.dto;

import pe.edu.cibertec.apipatrimonioservice.entity.EstadoTransferencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferenciaResponse(
        Integer idTransferencia,
        Integer idPuesto,
        Integer idContratoSaliente,
        Integer idSocioSaliente,
        Integer idSocioEntrante,
        Integer idContratoEntrante,
        Integer idUsuarioTramite,
        BigDecimal costoTransferencia,
        Boolean deudaValidada,
        Boolean asumeDeuda,
        BigDecimal montoDeudaAsumida,
        EstadoTransferencia estadoTransferencia,
        String observacion,
        LocalDateTime fechaTramite
) {
}
