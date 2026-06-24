package pe.edu.cibertec.apitransferenciaservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferenciaRequest {
    private Integer idPuesto;
    private Integer idSocioSaliente;
    private Integer idSocioEntrante;
    private Integer idUsuarioTramite;
    private BigDecimal costoTransferencia;
    private Boolean asumeDeuda;
}
