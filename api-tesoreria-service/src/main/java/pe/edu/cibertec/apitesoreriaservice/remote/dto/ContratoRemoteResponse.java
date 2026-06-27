package pe.edu.cibertec.apitesoreriaservice.remote.dto;

import java.time.LocalDate;

public record ContratoRemoteResponse(
        Integer idContrato,
        Integer idPuesto,
        String codigoPuesto,
        Integer idSocio,
        String nombreSocio,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estadoContrato,
        String motivoCierre,
        Integer idUsuarioRegistro
) {
}
