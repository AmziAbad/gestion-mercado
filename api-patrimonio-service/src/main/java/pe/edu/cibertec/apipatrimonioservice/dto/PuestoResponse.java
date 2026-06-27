package pe.edu.cibertec.apipatrimonioservice.dto;

import pe.edu.cibertec.apipatrimonioservice.entity.EstadoPuesto;

import java.time.LocalDateTime;

public record PuestoResponse(
        Integer idPuesto,
        String codigoPuesto,
        String pabellon,
        String medidas,
        String giro,
        EstadoPuesto estadoPuesto,
        LocalDateTime fechaRegistro
) {
}
