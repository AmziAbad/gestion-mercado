package pe.edu.cibertec.apipatrimonioservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pe.edu.cibertec.apipatrimonioservice.entity.EstadoPuesto;

public record PuestoRequest(
        @NotBlank @Size(max = 20) String codigoPuesto,
        @NotBlank @Size(max = 50) String pabellon,
        @Size(max = 50) String medidas,
        @Size(max = 80) String giro,
        EstadoPuesto estadoPuesto
) {
}
