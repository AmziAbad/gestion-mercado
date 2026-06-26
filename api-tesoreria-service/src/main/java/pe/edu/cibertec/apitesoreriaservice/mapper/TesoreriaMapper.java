package pe.edu.cibertec.apitesoreriaservice.mapper;

import pe.edu.cibertec.apitesoreriaservice.dto.ComprobanteResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.ConceptoResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.CuotaResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.DeudaPendienteReporteResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.FlujoCajaReporteResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.PagoResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.TurnoResponse;
import pe.edu.cibertec.apitesoreriaservice.entity.Comprobante;
import pe.edu.cibertec.apitesoreriaservice.entity.ConceptoCobro;
import pe.edu.cibertec.apitesoreriaservice.entity.CuotaDeuda;
import pe.edu.cibertec.apitesoreriaservice.entity.Pago;
import pe.edu.cibertec.apitesoreriaservice.entity.TurnoCaja;

public final class TesoreriaMapper {

    private TesoreriaMapper() {
    }

    public static ConceptoResponse toConceptoResponse(ConceptoCobro concepto) {
        return new ConceptoResponse(
                concepto.getIdConcepto(),
                concepto.getNombreConcepto(),
                concepto.getDescripcion(),
                concepto.getTipoCobro(),
                concepto.getPeriodicidad(),
                concepto.getMontoFijo(),
                concepto.getCostoTotalProrrateo(),
                concepto.getActivo(),
                concepto.getFechaRegistro()
        );
    }

    public static TurnoResponse toTurnoResponse(TurnoCaja turno) {
        return new TurnoResponse(
                turno.getIdTurno(),
                turno.getIdUsuario(),
                turno.getFechaApertura(),
                turno.getFechaCierre(),
                turno.getMontoInicial(),
                turno.getMontoRecaudado(),
                turno.getMontoEsperado(),
                turno.getDiferencia(),
                turno.getEstadoTurno(),
                turno.getObservacionApertura(),
                turno.getObservacionCierre()
        );
    }

    public static CuotaResponse toCuotaResponse(CuotaDeuda cuota) {
        return new CuotaResponse(
                cuota.getIdCuota(),
                cuota.getIdPuesto(),
                cuota.getIdContrato(),
                cuota.getIdConcepto(),
                cuota.getPeriodoMes(),
                cuota.getPeriodoAnio(),
                cuota.getMontoTotal(),
                cuota.getEstadoCuota(),
                cuota.getFechaGeneracion(),
                cuota.getFechaVencimiento(),
                cuota.getIdUsuarioGeneracion(),
                cuota.getMotivoExoneracion(),
                cuota.getMotivoAnulacion(),
                cuota.getIdCuotaOrigen(),
                cuota.getIdCuotaReemplazo()
        );
    }

    public static PagoResponse toPagoResponse(Pago pago, Comprobante comprobante) {
        return new PagoResponse(
                pago.getIdPago(),
                pago.getIdCuota(),
                pago.getIdTurno(),
                pago.getIdUsuarioCobro(),
                pago.getMetodoPago(),
                pago.getNumeroOperacion(),
                pago.getMontoPagado(),
                pago.getFechaPago(),
                pago.getEstadoPago(),
                pago.getMotivoExtorno(),
                pago.getFechaExtorno(),
                pago.getIdUsuarioExtorno(),
                comprobante != null ? toComprobanteResponse(comprobante) : null
        );
    }

    public static ComprobanteResponse toComprobanteResponse(Comprobante comprobante) {
        return new ComprobanteResponse(
                comprobante.getIdComprobante(),
                comprobante.getIdPago(),
                comprobante.getIdCuota(),
                comprobante.getNumeroComprobante(),
                comprobante.getFechaEmision(),
                comprobante.getMontoTotal(),
                comprobante.getMetodoPago(),
                comprobante.getEstadoComprobante()
        );
    }

    public static DeudaPendienteReporteResponse toDeudaPendiente(CuotaDeuda cuota) {
        return new DeudaPendienteReporteResponse(
                cuota.getIdCuota(),
                cuota.getIdPuesto(),
                cuota.getIdContrato(),
                cuota.getIdConcepto(),
                cuota.getPeriodoMes(),
                cuota.getPeriodoAnio(),
                cuota.getMontoTotal(),
                cuota.getFechaVencimiento()
        );
    }

    public static FlujoCajaReporteResponse toFlujoCaja(Pago pago) {
        return new FlujoCajaReporteResponse(
                pago.getIdPago(),
                pago.getIdTurno(),
                pago.getIdCuota(),
                pago.getIdUsuarioCobro(),
                pago.getMetodoPago(),
                pago.getMontoPagado(),
                pago.getFechaPago()
        );
    }
}
