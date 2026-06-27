package pe.edu.cibertec.apitesoreriaservice.dto;

import pe.edu.cibertec.apitesoreriaservice.entity.Periodicidad;
import pe.edu.cibertec.apitesoreriaservice.entity.TipoCobro;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConceptoResponse(
        Integer idConcepto,
        String nombreConcepto,
        String descripcion,
        TipoCobro tipoCobro,
        Periodicidad periodicidad,
        BigDecimal montoFijo,
        BigDecimal costoTotalProrrateo,
        Boolean activo,
        LocalDateTime fechaRegistro
) {
}
