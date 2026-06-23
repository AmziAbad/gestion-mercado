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

    CuotaPago generarDeudaEspecifica(Integer idPuesto, Integer idServicio, Double monto, Integer mes, Integer anio);

    CuotaPago exonerarCuota(Integer id, String motivo);

    CuotaPago revertirPago(Integer id);

    pe.edu.cibertec.apipagosservice.dto.ComprobanteDTO generarComprobante(Integer idCuota);
}
