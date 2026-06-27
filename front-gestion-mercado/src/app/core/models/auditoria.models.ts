export type TipoAnulacion = 'CUOTA' | 'PAGO' | 'COMPROBANTE' | 'TRANSFERENCIA';

export interface AuditoriaEvento {
  idEvento: number;
  modulo: string;
  tipoEvento: string;
  entidadAfectada: string;
  idRegistroAfectado: number;
  idUsuario: number;
  descripcion: string;
  fechaEvento: string;
}

export interface AuditoriaEventoRequest {
  modulo: string;
  tipoEvento: string;
  entidadAfectada: string;
  idRegistroAfectado: number | null;
  idUsuario: number | null;
  descripcion: string;
}

export interface AuditoriaAnulacion {
  idAuditoria: number;
  tipoAnulacion: TipoAnulacion;
  idRegistroAfectado: number;
  idUsuario: number;
  motivoSustento: string;
  fechaAnulacion: string;
}

export interface AuditoriaAnulacionRequest {
  tipoAnulacion: TipoAnulacion;
  idRegistroAfectado: number | null;
  idUsuario: number | null;
  motivoSustento: string;
}

export interface PadronHabil {
  idSocio: number;
  dni: string;
  nombres: string;
  apellidos: string;
  correo: string | null;
  telefono: string | null;
}
