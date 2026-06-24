package pe.edu.cibertec.apipagosservice.service.impl;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apipagosservice.dto.DetalleDeudaDTO;
import pe.edu.cibertec.apipagosservice.dto.DeudorDTO;
import pe.edu.cibertec.apipagosservice.dto.EstadoDeudoresDTO;
import pe.edu.cibertec.apipagosservice.dto.FlujoCajaDiarioDTO;
import pe.edu.cibertec.apipagosservice.service.ReportePdfService;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportePdfServiceImpl implements ReportePdfService {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public byte[] generarFlujoCajaDiarioPdf(FlujoCajaDiarioDTO reporte) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("fecha", reporte.getFecha().toString());
        parametros.put("totalPagos", reporte.getTotalPagos());
        parametros.put("totalRecaudado", reporte.getTotalRecaudado());

        List<Map<String, Object>> filas = reporte.getPagos().stream()
                .map(pago -> {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("fechaPago", pago.getFechaPago() != null ? pago.getFechaPago().format(DATE_TIME_FORMAT) : "");
                    fila.put("comprobante", valor(pago.getNumeroComprobante()));
                    fila.put("puesto", valor(pago.getNumeroPuesto()));
                    fila.put("socio", valor(pago.getNombreSocio()));
                    fila.put("servicio", valor(pago.getNombreServicio()));
                    fila.put("metodoPago", pago.getMetodoPago() != null ? pago.getMetodoPago().name() : "");
                    fila.put("monto", pago.getMonto() != null ? pago.getMonto() : 0.0);
                    return fila;
                })
                .toList();

        return generarPdf("reports/flujo-caja-diario.jrxml", parametros, filas);
    }

    @Override
    public byte[] generarEstadoDeudoresPdf(EstadoDeudoresDTO reporte) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("totalPuestosConDeuda", reporte.getTotalPuestosConDeuda());
        parametros.put("totalCuotasPendientes", reporte.getTotalCuotasPendientes());
        parametros.put("totalDeuda", reporte.getTotalDeuda());

        List<Map<String, Object>> filas = reporte.getDeudores().stream()
                .flatMap(deudor -> deudor.getCuotas().stream()
                        .map(cuota -> filaDeudor(deudor, cuota)))
                .toList();

        return generarPdf("reports/estado-deudores.jrxml", parametros, filas);
    }

    private Map<String, Object> filaDeudor(DeudorDTO deudor, DetalleDeudaDTO cuota) {
        Map<String, Object> fila = new HashMap<>();
        fila.put("puesto", valor(deudor.getNumeroPuesto()));
        fila.put("pabellon", valor(deudor.getPabellon()));
        fila.put("socio", valor(deudor.getNombreSocio()));
        fila.put("servicio", valor(cuota.getNombreServicio()));
        fila.put("periodo", cuota.getMes() + "/" + cuota.getAnio());
        fila.put("monto", cuota.getMonto() != null ? cuota.getMonto() : 0.0);
        fila.put("totalPuesto", deudor.getTotalDeuda() != null ? deudor.getTotalDeuda() : 0.0);
        return fila;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private byte[] generarPdf(String plantilla, Map<String, Object> parametros, Collection<Map<String, Object>> filas) {
        System.setProperty("java.awt.headless", "true");
        try (InputStream inputStream = new ClassPathResource(plantilla).getInputStream()) {
            JasperReport reporte = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    reporte,
                    parametros,
                    new JRMapCollectionDataSource((Collection) filas)
            );
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (IOException | JRException e) {
            throw new RuntimeException("No se pudo generar el reporte PDF", e);
        }
    }

    private String valor(String texto) {
        return texto != null && !texto.isBlank() ? texto : "-";
    }
}
