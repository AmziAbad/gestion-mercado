export type EstadoSocio = 'ACTIVO' | 'INACTIVO';
export type EstadoPuesto = 'LIBRE' | 'OCUPADO' | 'MANTENIMIENTO';
export type EstadoContrato = 'ACTIVO' | 'FINALIZADO' | 'ANULADO';
export type EstadoTransferencia = 'REGISTRADA' | 'BLOQUEADA' | 'ANULADA';

export interface Socio {
  idSocio: number;
  dni: string;
  ruc: string | null;
  nombres: string;
  apellidos: string;
  telefono: string | null;
  correo: string | null;
  direccion: string | null;
  estado: EstadoSocio;
  esAsociacion: boolean;
  fechaRegistro: string;
}

export interface SocioRequest {
  dni: string;
  ruc: string | null;
  nombres: string;
  apellidos: string;
  telefono: string | null;
  correo: string | null;
  direccion: string | null;
  estado: EstadoSocio;
  esAsociacion: boolean;
}

export interface Puesto {
  idPuesto: number;
  codigoPuesto: string;
  pabellon: string;
  medidas: string | null;
  giro: string | null;
  estadoPuesto: EstadoPuesto;
  fechaRegistro: string;
}

export interface PuestoRequest {
  codigoPuesto: string;
  pabellon: string;
  medidas: string | null;
  giro: string | null;
  estadoPuesto: EstadoPuesto;
}

export interface Contrato {
  idContrato: number;
  idPuesto: number;
  codigoPuesto: string;
  idSocio: number;
  nombreSocio: string;
  fechaInicio: string;
  fechaFin: string | null;
  estadoContrato: EstadoContrato;
  motivoCierre: string | null;
  idUsuarioRegistro: number;
  fechaRegistro: string;
}

export interface ContratoRequest {
  idPuesto: number | null;
  idSocio: number | null;
  fechaInicio: string | null;
}

export interface FinalizarContratoRequest {
  motivoCierre: string;
}

export interface Saneamiento {
  idPuesto: number;
  tieneDeuda: boolean;
  cantidadCuotasPendientes: number;
  totalPendiente: number;
  mensaje: string;
}

export interface Transferencia {
  idTransferencia: number;
  idPuesto: number;
  idContratoSaliente: number | null;
  idSocioSaliente: number | null;
  idSocioEntrante: number;
  idContratoEntrante: number | null;
  idUsuarioTramite: number;
  costoTransferencia: number;
  deudaValidada: boolean;
  asumeDeuda: boolean;
  montoDeudaAsumida: number;
  estadoTransferencia: EstadoTransferencia;
  observacion: string | null;
  fechaTramite: string;
}

export interface TransferenciaRequest {
  idPuesto: number | null;
  idSocioEntrante: number | null;
  costoTransferencia: number;
  asumeDeuda: boolean;
  observacion: string | null;
  fechaInicio: string | null;
}
