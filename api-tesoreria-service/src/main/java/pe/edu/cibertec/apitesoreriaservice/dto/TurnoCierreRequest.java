package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.Size;

public record TurnoCierreRequest(
        @Size(max = 255) String observacionCierre
) {
}
