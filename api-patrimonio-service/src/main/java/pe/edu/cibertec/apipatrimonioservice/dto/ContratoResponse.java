package pe.edu.cibertec.apipatrimonioservice.dto;

import pe.edu.cibertec.apipatrimonioservice.entity.EstadoContrato;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContratoResponse(
        Integer idContrato,
        Integer idPuesto,
        String codigoPuesto,
        Integer idSocio,
        String nombreSocio,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoContrato estadoContrato,
        String motivoCierre,
        Integer idUsuarioRegistro,
        LocalDateTime fechaRegistro
) {
}
