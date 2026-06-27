package pe.edu.cibertec.apitesoreriaservice.dto;

import pe.edu.cibertec.apitesoreriaservice.entity.EstadoTurno;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TurnoResponse(
        Integer idTurno,
        Integer idUsuario,
        LocalDateTime fechaApertura,
        LocalDateTime fechaCierre,
        BigDecimal montoInicial,
        BigDecimal montoRecaudado,
        BigDecimal montoEsperado,
        BigDecimal diferencia,
        EstadoTurno estadoTurno,
        String observacionApertura,
        String observacionCierre
) {
}
