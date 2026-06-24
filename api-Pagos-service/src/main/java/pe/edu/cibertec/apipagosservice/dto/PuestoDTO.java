package pe.edu.cibertec.apipagosservice.dto;

import lombok.Data;

@Data
public class PuestoDTO {
    private Integer idPuesto;
    private String numeroPuesto;
    private String pabellon;
    private String estadoPuesto;
    private Integer idSocioActual;
}
