import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { API_ENDPOINTS } from '../constants/api-endpoints';
import {
  AuditoriaAnulacion,
  AuditoriaAnulacionRequest,
  AuditoriaEvento,
  AuditoriaEventoRequest,
  PadronHabil,
} from '../models/auditoria.models';
import { DeudaPendienteReporte, FlujoCajaReporte } from '../models/tesoreria.models';

@Injectable({
  providedIn: 'root',
})
export class AuditoriaReportesApi {
  constructor(private readonly http: HttpClient) {}

  listarEventos() {
    return this.http.get<AuditoriaEvento[]>(`${API_ENDPOINTS.auditoria}/eventos`);
  }

  listarEventosPorRegistro(entidadAfectada: string, idRegistroAfectado: number) {
    return this.http.get<AuditoriaEvento[]>(
      `${API_ENDPOINTS.auditoria}/eventos/${encodeURIComponent(entidadAfectada)}/${idRegistroAfectado}`,
    );
  }

  registrarEvento(request: AuditoriaEventoRequest) {
    return this.http.post<AuditoriaEvento>(`${API_ENDPOINTS.auditoria}/eventos`, request);
  }

  listarAnulaciones() {
    return this.http.get<AuditoriaAnulacion[]>(`${API_ENDPOINTS.auditoria}/anulaciones`);
  }

  listarAnulacionesPorRegistro(tipoAnulacion: string, idRegistroAfectado: number) {
    return this.http.get<AuditoriaAnulacion[]>(
      `${API_ENDPOINTS.auditoria}/anulaciones/${encodeURIComponent(tipoAnulacion)}/${idRegistroAfectado}`,
    );
  }

  registrarAnulacion(request: AuditoriaAnulacionRequest) {
    return this.http.post<AuditoriaAnulacion>(`${API_ENDPOINTS.auditoria}/anulaciones`, request);
  }

  padronHabiles() {
    return this.http.get<PadronHabil[]>(`${API_ENDPOINTS.reportes}/padron-habiles`);
  }

  morosidad() {
    return this.http.get<DeudaPendienteReporte[]>(`${API_ENDPOINTS.reportes}/morosidad`);
  }

  flujoCajaDiario(fecha?: string, idTurno?: number | null) {
    const params = this.reporteParams(fecha, idTurno);
    return this.http.get<FlujoCajaReporte[]>(`${API_ENDPOINTS.reportes}/flujo-caja-diario`, {
      params: params.keys().length ? params : undefined,
    });
  }

  reportePdfUrl(
    tipo: 'padron-habiles' | 'morosidad' | 'flujo-caja-diario',
    fecha?: string,
    idTurno?: number | null,
  ): string {
    const params = this.reporteParams(fecha, idTurno);
    const query = params.keys().length ? `?${params.toString()}` : '';
    return `${API_ENDPOINTS.reportes}/${tipo}/pdf${query}`;
  }

  reportePdf(
    tipo: 'padron-habiles' | 'morosidad' | 'flujo-caja-diario',
    fecha?: string,
    idTurno?: number | null,
  ) {
    const params = this.reporteParams(fecha, idTurno);
    return this.http.get(`${API_ENDPOINTS.reportes}/${tipo}/pdf`, {
      params: params.keys().length ? params : undefined,
      responseType: 'blob',
    });
  }

  private reporteParams(fecha?: string, idTurno?: number | null): HttpParams {
    let params = new HttpParams();
    if (fecha) {
      params = params.set('fecha', fecha);
    }
    if (idTurno !== undefined && idTurno !== null) {
      params = params.set('idTurno', idTurno);
    }
    return params;
  }
}
