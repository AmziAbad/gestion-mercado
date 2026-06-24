package pe.edu.cibertec.apipagosservice.service;

import pe.edu.cibertec.apipagosservice.dto.ComprobanteDTO;
import pe.edu.cibertec.apipagosservice.dto.DeudaDTO;
import pe.edu.cibertec.apipagosservice.dto.EstadoDeudoresDTO;
import pe.edu.cibertec.apipagosservice.dto.FlujoCajaDiarioDTO;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;

import java.time.LocalDate;
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

    CuotaPago anularCuota(Integer id, String motivo);

    CuotaPago anularYReemplazarCuota(Integer id, String motivo, Integer idServicio, Double monto, Integer mes, Integer anio);

    CuotaPago revertirPago(Integer id, String motivo);

    ComprobanteDTO generarComprobante(Integer idCuota);

    FlujoCajaDiarioDTO obtenerFlujoCajaDiario(LocalDate fecha);

    EstadoDeudoresDTO obtenerEstadoDeudores();
}
