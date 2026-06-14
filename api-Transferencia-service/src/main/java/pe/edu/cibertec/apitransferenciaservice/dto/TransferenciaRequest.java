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
    // Este campo es solo para validación en la lógica (no va a la BD)
    private Boolean asumeDeuda;
}
