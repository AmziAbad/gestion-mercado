import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { API_ENDPOINTS } from '../constants/api-endpoints';
import {
  Comprobante,
  Concepto,
  ConceptoRequest,
  Cuota,
  CuotaAnulacionRequest,
  CuotaEspecificaRequest,
  CuotaExoneracionRequest,
  CuotaMasivaRequest,
  DeudaPendienteReporte,
  EstadoCuenta,
  FlujoCajaReporte,
  Pago,
  PagoExtornoRequest,
  PagoRequest,
  Turno,
  TurnoAperturaRequest,
  TurnoCierreRequest,
} from '../models/tesoreria.models';

@Injectable({
  providedIn: 'root',
})
export class TesoreriaApi {
  constructor(private readonly http: HttpClient) {}

  listarConceptos() {
    return this.http.get<Concepto[]>(API_ENDPOINTS.conceptos);
  }

  crearConcepto(request: ConceptoRequest) {
    return this.http.post<Concepto>(API_ENDPOINTS.conceptos, request);
  }

  actualizarConcepto(idConcepto: number, request: ConceptoRequest) {
    return this.http.put<Concepto>(`${API_ENDPOINTS.conceptos}/${idConcepto}`, request);
  }

  cambiarEstadoConcepto(idConcepto: number, activo: boolean) {
    return this.http.patch<Concepto>(`${API_ENDPOINTS.conceptos}/${idConcepto}/estado`, null, {
      params: { activo: activo.toString() }
    });
  }

  activarConcepto(idConcepto: number) {
    return this.http.post<Concepto>(`${API_ENDPOINTS.conceptos}/${idConcepto}/activar`, {});
  }

  desactivarConcepto(idConcepto: number) {
    return this.http.post<Concepto>(`${API_ENDPOINTS.conceptos}/${idConcepto}/desactivar`, {});
  }

  listarCuotas() {
    return this.http.get<Cuota[]>(API_ENDPOINTS.cuotas);
  }

  obtenerCuota(idCuota: number) {
    return this.http.get<Cuota>(`${API_ENDPOINTS.cuotas}/${idCuota}`);
  }

  generarCuotasMasivas(request: CuotaMasivaRequest) {
    return this.http.post<Cuota[]>(`${API_ENDPOINTS.cuotas}/masivas`, request);
  }

  generarCuotaEspecifica(request: CuotaEspecificaRequest) {
    return this.http.post<Cuota>(`${API_ENDPOINTS.cuotas}/especifica`, request);
  }

  anularCuota(idCuota: number, request: CuotaAnulacionRequest) {
    return this.http.patch<Cuota>(`${API_ENDPOINTS.cuotas}/${idCuota}/anular`, request);
  }

  exonerarCuota(idCuota: number, request: CuotaExoneracionRequest) {
    return this.http.patch<Cuota>(`${API_ENDPOINTS.cuotas}/${idCuota}/exonerar`, request);
  }

  estadoCuentaPorPuesto(idPuesto: number) {
    return this.http.get<EstadoCuenta>(`${API_ENDPOINTS.estadosCuenta}/puesto/${idPuesto}`);
  }

  resumenDeudaPorPuesto(idPuesto: number) {
    return this.http.get<EstadoCuenta>(`${API_ENDPOINTS.estadosCuenta}/puesto/${idPuesto}/resumen`);
  }

  estadoCuentaPorSocio(idSocio: number) {
    return this.http.get<EstadoCuenta>(`${API_ENDPOINTS.estadosCuenta}/socio/${idSocio}`);
  }

  estadoCuentaPorDni(dni: string) {
    return this.http.get<EstadoCuenta>(`${API_ENDPOINTS.estadosCuenta}/dni/${dni}`);
  }

  listarTurnos(fecha?: string) {
    const params = fecha ? new HttpParams().set('fecha', fecha) : undefined;
    return this.http.get<Turno[]>(API_ENDPOINTS.turnos, { params });
  }

  aperturarTurno(request: TurnoAperturaRequest) {
    return this.http.post<Turno>(`${API_ENDPOINTS.turnos}/aperturar`, request);
  }

  cerrarTurno(idTurno: number, request: TurnoCierreRequest) {
    return this.http.post<Turno>(`${API_ENDPOINTS.turnos}/${idTurno}/cerrar`, request);
  }

  obtenerPago(idPago: number) {
    return this.http.get<Pago>(`${API_ENDPOINTS.pagos}/${idPago}`);
  }

  registrarPago(request: PagoRequest) {
    return this.http.post<Pago>(API_ENDPOINTS.pagos, request);
  }

  extornarPago(idPago: number, request: PagoExtornoRequest) {
    return this.http.patch<Pago>(`${API_ENDPOINTS.pagos}/${idPago}/extornar`, request);
  }

  obtenerComprobante(idComprobante: number) {
    return this.http.get<Comprobante>(`${API_ENDPOINTS.comprobantes}/${idComprobante}`);
  }

  obtenerComprobantePorPago(idPago: number) {
    return this.http.get<Comprobante>(`${API_ENDPOINTS.comprobantes}/pago/${idPago}`);
  }

  datosMorosidad() {
    return this.http.get<DeudaPendienteReporte[]>(`${API_ENDPOINTS.reportes}/morosidad/datos`);
  }

  datosFlujoCaja(fecha?: string) {
    const params = fecha ? new HttpParams().set('fecha', fecha) : undefined;
    return this.http.get<FlujoCajaReporte[]>(`${API_ENDPOINTS.reportes}/flujo-caja-diario/datos`, {
      params,
    });
  }
}
