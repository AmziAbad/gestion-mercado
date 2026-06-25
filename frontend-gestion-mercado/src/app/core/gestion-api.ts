import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Socio {
  idSocio?: number;
  dni: string;
  ruc?: string;
  nombre: string;
  apellido: string;
  telefono?: string;
  correo?: string;
  direccion?: string;
  estadoSolvencia?: boolean;
  activo: boolean;
  esAsociacion?: boolean;
}

export interface Puesto {
  idPuesto?: number;
  numeroPuesto: string;
  pabellon: string;
  medidas?: string;
  precio?: number;
  estadoPuesto: string;
  idSocioActual?: number;
}

export interface Servicio {
  idServicio?: number;
  nombre?: string;
  nombreServicio: string;
  tipoCobro: string;
  costoTotalExterno?: number;
  montoFijoPuesto?: number;
  activo: boolean;
}

export interface CuotaPago {
  idCuota: number;
  idPuesto: number;
  idServicio: number;
  mes: number;
  anio: number;
  monto: number;
  estado: string;
  fechaPago?: string;
  metodoPago?: string;
  numeroOperacion?: string;
  numeroComprobante?: string;
}

export interface GenerarCuotasResponse {
  total: number;
  mensaje: string;
}

export interface PagoCuotaRequest {
  metodoPago: string;
  numeroOperacion?: string;
}

export interface Comprobante {
  titulo: string;
  idCuota: number;
  numeroOperacion?: string;
  numeroComprobante: string;
  fechaEmision?: string;
  montoPagado: number;
  metodoPago?: string;
  idPuesto: number;
  numeroPuesto?: string;
  idServicio: number;
  nombreServicio?: string;
  periodo?: string;
  mensaje?: string;
}

export interface Transferencia {
  idTransferencia?: number;
  idPuesto: number;
  idSocioSaliente?: number;
  idSocioEntrante: number;
  idUsuarioTramite: number;
  costoTransferencia?: number;
  fechaTramite?: string;
  asumeDeuda?: boolean;
  montoDeudaAsumida?: number;
  observacion?: string;
}

export interface Usuario {
  idUsuario?: number;
  username: string;
  password?: string;
  nombreCompleto: string;
  dni: string;
  correo?: string;
  telefono?: string;
  rol: string;
  activo: boolean;
}

export interface DetalleFlujoCaja {
  idCuota: number;
  numeroPuesto?: string;
  nombreSocio?: string;
  nombreServicio?: string;
  monto: number;
  metodoPago?: string;
  numeroComprobante?: string;
  fechaPago?: string;
}

export interface FlujoCajaDiario {
  fecha: string;
  totalPagos: number;
  totalRecaudado: number;
  pagos: DetalleFlujoCaja[];
}

export interface Deudor {
  idPuesto: number;
  numeroPuesto?: string;
  pabellon?: string;
  idSocio?: number;
  nombreSocio?: string;
  totalCuotasPendientes: number;
  totalDeuda: number;
}

export interface EstadoDeudores {
  totalPuestosConDeuda: number;
  totalCuotasPendientes: number;
  totalDeuda: number;
  deudores: Deudor[];
}

@Injectable({
  providedIn: 'root',
})
export class GestionApi {
  constructor(private readonly http: HttpClient) {}

  listarSocios() {
    return this.http.get<Socio[]>('/api-Socio-service/api/v1/socios');
  }

  buscarSocioPorDni(dni: string) {
    return this.http.get<Socio>(`/api-Socio-service/api/v1/socios/buscar/${dni}`);
  }

  guardarSocio(socio: Socio) {
    return socio.idSocio
      ? this.http.put<Socio>(`/api-Socio-service/api/v1/socios/${socio.idSocio}`, socio)
      : this.http.post<Socio>('/api-Socio-service/api/v1/socios', socio);
  }

  eliminarSocio(idSocio: number) {
    return this.http.delete<void>(`/api-Socio-service/api/v1/socios/${idSocio}`);
  }

  listarPuestos() {
    return this.http.get<Puesto[]>('/api-Puesto-service/api/v1/puestos');
  }

  listarPuestosOcupados() {
    return this.http.get<Puesto[]>('/api-Puesto-service/api/v1/puestos/ocupados');
  }

  listarPuestosPorPabellon(pabellon: string) {
    return this.http.get<Puesto[]>(`/api-Puesto-service/api/v1/puestos/pabellon/${pabellon}`);
  }

  guardarPuesto(puesto: Puesto) {
    return puesto.idPuesto
      ? this.http.put<Puesto>(`/api-Puesto-service/api/v1/puestos/${puesto.idPuesto}`, puesto)
      : this.http.post<Puesto>('/api-Puesto-service/api/v1/puestos', puesto);
  }

  eliminarPuesto(idPuesto: number) {
    return this.http.delete<void>(`/api-Puesto-service/api/v1/puestos/${idPuesto}`);
  }

  listarServicios() {
    return this.http.get<Servicio[]>('/api-Servicio-service/api/v1/servicios');
  }

  listarServiciosActivos() {
    return this.http.get<Servicio[]>('/api-Servicio-service/api/v1/servicios/activos');
  }

  guardarServicio(servicio: Servicio) {
    return servicio.idServicio
      ? this.http.put<Servicio>(
          `/api-Servicio-service/api/v1/servicios/${servicio.idServicio}`,
          servicio,
        )
      : this.http.post<Servicio>('/api-Servicio-service/api/v1/servicios', servicio);
  }

  activarServicio(idServicio: number) {
    return this.http.post<Servicio>(
      `/api-Servicio-service/api/v1/servicios/${idServicio}/activar`,
      null,
    );
  }

  desactivarServicio(idServicio: number) {
    return this.http.post<Servicio>(
      `/api-Servicio-service/api/v1/servicios/${idServicio}/desactivar`,
      null,
    );
  }

  eliminarServicio(idServicio: number) {
    return this.http.delete<void>(`/api-Servicio-service/api/v1/servicios/${idServicio}`);
  }

  listarCuotas() {
    return this.http.get<CuotaPago[]>('/api-Pagos-service/api/v1/pagos');
  }

  generarCuotas(mes: number, anio: number) {
    return this.http.post<GenerarCuotasResponse>('/api-Pagos-service/api/v1/pagos/generar', null, {
      params: {
        mes,
        anio,
      },
    });
  }

  pagarCuota(idCuota: number, pago: PagoCuotaRequest) {
    return this.http.post<CuotaPago>(
      `/api-Pagos-service/api/v1/pagos/cuotas/${idCuota}/pagar`,
      pago,
    );
  }

  generarComprobante(idCuota: number) {
    return this.http.get<Comprobante>(
      `/api-Pagos-service/api/v1/pagos/cuotas/${idCuota}/comprobante`,
    );
  }

  obtenerFlujoCaja(fecha: string) {
    return this.http.get<FlujoCajaDiario>('/api-Pagos-service/api/v1/pagos/reportes/flujo-caja', {
      params: { fecha },
    });
  }

  obtenerDeudores() {
    return this.http.get<EstadoDeudores>('/api-Pagos-service/api/v1/pagos/reportes/deudores');
  }

  obtenerDeudaPorPuesto(idPuesto: number) {
    return this.http.get<{ totalDeuda: number; tieneDeuda: boolean }>(
      `/api-Pagos-service/api/v1/pagos/deuda/puesto/${idPuesto}`,
    );
  }

  descargarFlujoCajaPdf(fecha: string) {
    return this.http.get('/api-Pagos-service/api/v1/pagos/reportes/flujo-caja/pdf', {
      params: { fecha },
      responseType: 'blob',
    });
  }

  descargarDeudoresPdf() {
    return this.http.get('/api-Pagos-service/api/v1/pagos/reportes/deudores/pdf', {
      responseType: 'blob',
    });
  }

  listarTransferencias() {
    return this.http.get<Transferencia[]>('/api-Transferencia-service/api/v1/transferencias');
  }

  registrarTransferencia(transferencia: Transferencia) {
    return this.http.post<Transferencia>(
      '/api-Transferencia-service/api/v1/transferencias',
      transferencia,
    );
  }

  listarUsuarios() {
    return this.http.get<Usuario[]>('/api-UsuarioLogin-service/usuarios');
  }

  guardarUsuario(usuario: Usuario) {
    return usuario.idUsuario
      ? this.http.put<Usuario>(`/api-UsuarioLogin-service/usuarios/${usuario.idUsuario}`, usuario)
      : this.http.post<Usuario>('/api-UsuarioLogin-service/usuarios', usuario);
  }

  eliminarUsuario(idUsuario: number) {
    return this.http.delete<void>(`/api-UsuarioLogin-service/usuarios/${idUsuario}`);
  }
}
