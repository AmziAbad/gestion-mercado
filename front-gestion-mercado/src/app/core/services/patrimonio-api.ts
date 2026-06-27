import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_ENDPOINTS } from '../constants/api-endpoints';
import {
  Contrato,
  ContratoRequest,
  FinalizarContratoRequest,
  Puesto,
  PuestoRequest,
  Saneamiento,
  Socio,
  SocioRequest,
  Transferencia,
  TransferenciaRequest,
} from '../models/patrimonio.models';

@Injectable({
  providedIn: 'root',
})
export class PatrimonioApi {
  constructor(private readonly http: HttpClient) {}

  listarSocios() {
    return this.http.get<Socio[]>(API_ENDPOINTS.socios);
  }

  sociosActivosConContrato() {
    return this.http.get<Socio[]>(`${API_ENDPOINTS.socios}/activos-con-contrato`);
  }

  obtenerSocio(idSocio: number) {
    return this.http.get<Socio>(`${API_ENDPOINTS.socios}/${idSocio}`);
  }

  buscarSocioPorDni(dni: string) {
    return this.http.get<Socio>(`${API_ENDPOINTS.socios}/dni/${dni}`);
  }

  crearSocio(request: SocioRequest) {
    return this.http.post<Socio>(API_ENDPOINTS.socios, request);
  }

  actualizarSocio(idSocio: number, request: SocioRequest) {
    return this.http.put<Socio>(`${API_ENDPOINTS.socios}/${idSocio}`, request);
  }

  listarPuestos() {
    return this.http.get<Puesto[]>(API_ENDPOINTS.puestos);
  }

  obtenerPuesto(idPuesto: number) {
    return this.http.get<Puesto>(`${API_ENDPOINTS.puestos}/${idPuesto}`);
  }

  crearPuesto(request: PuestoRequest) {
    return this.http.post<Puesto>(API_ENDPOINTS.puestos, request);
  }

  actualizarPuesto(idPuesto: number, request: PuestoRequest) {
    return this.http.put<Puesto>(`${API_ENDPOINTS.puestos}/${idPuesto}`, request);
  }

  consultarSaneamiento(idPuesto: number) {
    return this.http.get<Saneamiento>(`${API_ENDPOINTS.puestos}/${idPuesto}/saneamiento`);
  }

  listarContratosActivos() {
    return this.http.get<Contrato[]>(`${API_ENDPOINTS.contratos}/activos`);
  }

  obtenerContrato(idContrato: number) {
    return this.http.get<Contrato>(`${API_ENDPOINTS.contratos}/${idContrato}`);
  }

  obtenerContratoActivoPorPuesto(idPuesto: number) {
    return this.http.get<Contrato>(`${API_ENDPOINTS.contratos}/puesto/${idPuesto}/activo`);
  }

  contratosActivosPorSocio(idSocio: number) {
    return this.http.get<Contrato[]>(`${API_ENDPOINTS.contratos}/socio/${idSocio}/activos`);
  }

  aperturarContrato(request: ContratoRequest) {
    return this.http.post<Contrato>(API_ENDPOINTS.contratos, request);
  }

  finalizarContrato(idContrato: number, request: FinalizarContratoRequest) {
    return this.http.post<Contrato>(`${API_ENDPOINTS.contratos}/${idContrato}/finalizar`, request);
  }

  listarTransferencias() {
    return this.http.get<Transferencia[]>(API_ENDPOINTS.transferencias);
  }

  obtenerTransferencia(idTransferencia: number) {
    return this.http.get<Transferencia>(`${API_ENDPOINTS.transferencias}/${idTransferencia}`);
  }

  registrarTransferencia(request: TransferenciaRequest) {
    return this.http.post<Transferencia>(API_ENDPOINTS.transferencias, request);
  }
}
