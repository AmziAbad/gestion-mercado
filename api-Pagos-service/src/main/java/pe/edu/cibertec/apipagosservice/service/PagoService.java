package pe.edu.cibertec.apipagosservice.service;

import pe.edu.cibertec.apipagosservice.dto.DeudaDTO;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;

import java.util.List;
import java.util.Map;

public interface PagoService {
    List<CuotaPago> listarPagos();

    int generarCuotasMensuales(int mes, int anio);

    CuotaPago pagarCuota(Integer id, Map<String, String> pago);

    List<CuotaPago> listarCuotasPorPuesto(Integer idPuesto);

    List<CuotaPago> listarCuotasPendientesPorPuesto(Integer idPuesto);

    DeudaDTO obtenerDeudaPorPuesto(Integer idPuesto);

    DeudaDTO obtenerDeudaPorSocio(Integer idSocio);
}
