package pe.edu.cibertec.apipuestoservice.dto;

import lombok.Data;
import pe.edu.cibertec.apipuestoservice.entity.EstadoPuesto;

@Data
public class ActualizarTitularRequest {
    private Integer idSocioActual;
    private EstadoPuesto estadoPuesto;
}
