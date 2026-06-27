package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.cibertec.apitesoreriaservice.entity.MetodoPago;

public record PagoRequest(
        @NotNull Integer idCuota,
        @NotNull MetodoPago metodoPago,
        @Size(max = 80) String numeroOperacion
) {
}
