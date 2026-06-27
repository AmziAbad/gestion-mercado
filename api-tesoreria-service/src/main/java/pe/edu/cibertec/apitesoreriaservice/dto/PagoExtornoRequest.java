package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PagoExtornoRequest(
        @NotBlank @Size(max = 255) String motivoExtorno
) {
}
