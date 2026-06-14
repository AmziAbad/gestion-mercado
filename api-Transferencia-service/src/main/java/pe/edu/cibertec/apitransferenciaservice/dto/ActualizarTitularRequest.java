package pe.edu.cibertec.apitransferenciaservice.dto;

import lombok.Data;

@Data
public class ActualizarTitularRequest {
    private Integer idSocioActual;
    private String estadoPuesto;
}
