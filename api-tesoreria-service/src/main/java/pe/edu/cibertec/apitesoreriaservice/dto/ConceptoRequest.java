package pe.edu.cibertec.apitesoreriaservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.cibertec.apitesoreriaservice.entity.Periodicidad;
import pe.edu.cibertec.apitesoreriaservice.entity.TipoCobro;

import java.math.BigDecimal;

public record ConceptoRequest(
        @NotBlank @Size(max = 100) String nombreConcepto,
        @Size(max = 255) String descripcion,
        @NotNull TipoCobro tipoCobro,
        @NotNull Periodicidad periodicidad,
        @DecimalMin("0.01") BigDecimal montoFijo,
        @DecimalMin("0.01") BigDecimal costoTotalProrrateo,
        Boolean activo
) {
}
