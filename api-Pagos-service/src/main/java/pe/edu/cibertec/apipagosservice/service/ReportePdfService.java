package pe.edu.cibertec.apipagosservice.service;

import pe.edu.cibertec.apipagosservice.dto.EstadoDeudoresDTO;
import pe.edu.cibertec.apipagosservice.dto.FlujoCajaDiarioDTO;

public interface ReportePdfService {
    byte[] generarFlujoCajaDiarioPdf(FlujoCajaDiarioDTO reporte);

    byte[] generarEstadoDeudoresPdf(EstadoDeudoresDTO reporte);
}
