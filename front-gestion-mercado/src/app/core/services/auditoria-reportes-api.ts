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

  registrarEvento(request: AuditoriaEventoRequest) {
    return this.http.post<AuditoriaEvento>(`${API_ENDPOINTS.auditoria}/eventos`, request);
  }

  listarAnulaciones() {
    return this.http.get<AuditoriaAnulacion[]>(`${API_ENDPOINTS.auditoria}/anulaciones`);
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

  flujoCajaDiario(fecha?: string) {
    const params = fecha ? new HttpParams().set('fecha', fecha) : undefined;
    return this.http.get<FlujoCajaReporte[]>(`${API_ENDPOINTS.reportes}/flujo-caja-diario`, {
      params,
    });
  }

  reportePdfUrl(
    tipo: 'padron-habiles' | 'morosidad' | 'flujo-caja-diario',
    fecha?: string,
  ): string {
    const fechaQuery = fecha ? `?fecha=${encodeURIComponent(fecha)}` : '';
    return `${API_ENDPOINTS.reportes}/${tipo}/pdf${fechaQuery}`;
  }

  reportePdf(tipo: 'padron-habiles' | 'morosidad' | 'flujo-caja-diario', fecha?: string) {
    const params = fecha ? new HttpParams().set('fecha', fecha) : undefined;
    return this.http.get(`${API_ENDPOINTS.reportes}/${tipo}/pdf`, {
      params,
      responseType: 'blob',
    });
  }
}
