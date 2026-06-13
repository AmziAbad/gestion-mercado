package pe.edu.cibertec.apipagosservice.dto;

import lombok.Data;

@Data
public class ServicioDTO {
    private Integer idServicio;
    private String nombreServicio;
    private String tipoCobro;
    private Double costoTotalExterno;
    private Double montoFijoPuesto;
    private Boolean activo;
}
