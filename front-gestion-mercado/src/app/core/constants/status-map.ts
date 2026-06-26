export type StatusTone = 'success' | 'warning' | 'danger' | 'info' | 'muted' | 'neutral';

export const STATUS_TONES: Record<string, StatusTone> = {
  ACTIVO: 'success',
  INACTIVO: 'muted',
  LIBRE: 'info',
  OCUPADO: 'success',
  MANTENIMIENTO: 'warning',
  FINALIZADO: 'muted',
  ANULADO: 'danger',
  PENDIENTE: 'warning',
  PAGADO: 'success',
  EXONERADO: 'info',
  REGISTRADO: 'success',
  REGISTRADA: 'success',
  EXTORNADO: 'danger',
  EMITIDO: 'success',
  ABIERTO: 'success',
  CERRADO: 'muted',
  BLOQUEADA: 'danger',
  ADMIN: 'info',
  TESORERO: 'success',
  RECEPCIONISTA: 'neutral',
};
