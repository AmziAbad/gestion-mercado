package pe.edu.cibertec.apipatrimonioservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferenciaRequest(
        @NotNull Integer idPuesto,
        @NotNull Integer idSocioEntrante,
        @DecimalMin("0.00") BigDecimal costoTransferencia,
        Boolean asumeDeuda,
        @Size(max = 500) String observacion,
        LocalDate fechaInicio
) {
}
