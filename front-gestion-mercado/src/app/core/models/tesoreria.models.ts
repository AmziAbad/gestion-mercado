export type TipoCobro = 'FIJO' | 'PRORRATEO';
export type Periodicidad = 'MENSUAL' | 'DIARIO' | 'EXTRAORDINARIO';
export type EstadoCuota = 'PENDIENTE' | 'PAGADO' | 'ANULADO' | 'EXONERADO';
export type MetodoPago = 'EFECTIVO' | 'TRANSFERENCIA' | 'YAPE_PLIN' | 'TARJETA';
export type EstadoPago = 'REGISTRADO' | 'EXTORNADO';
export type EstadoComprobante = 'EMITIDO' | 'ANULADO';
export type EstadoTurno = 'ABIERTO' | 'CERRADO' | 'ANULADO';

export interface Concepto {
  idConcepto: number;
  nombreConcepto: string;
  descripcion: string | null;
  tipoCobro: TipoCobro;
  periodicidad: Periodicidad;
  montoFijo: number | null;
  costoTotalProrrateo: number | null;
  activo: boolean;
  fechaRegistro: string;
}

export interface ConceptoRequest {
  nombreConcepto: string;
  descripcion: string | null;
  tipoCobro: TipoCobro;
  periodicidad: Periodicidad;
  montoFijo: number | null;
  costoTotalProrrateo: number | null;
  activo: boolean;
}

export interface Cuota {
  idCuota: number;
  idPuesto: number;
  idContrato: number;
  idConcepto: number;
  periodoMes: number;
  periodoAnio: number;
  montoTotal: number;
  estadoCuota: EstadoCuota;
  fechaGeneracion: string;
  fechaVencimiento: string | null;
  idUsuarioGeneracion: number;
  motivoExoneracion: string | null;
  motivoAnulacion: string | null;
  idCuotaOrigen: number | null;
  idCuotaReemplazo: number | null;
}

export interface CuotaMasivaRequest {
  idConcepto: number | null;
  periodoMes: number | null;
  periodoAnio: number | null;
  fechaVencimiento: string | null;
}

export interface CuotaEspecificaRequest extends CuotaMasivaRequest {
  idPuesto: number | null;
  idContrato: number | null;
  montoTotal: number | null;
}

export interface CuotaAnulacionRequest {
  motivoAnulacion: string;
  generarReemplazo: boolean;
  montoReemplazo: number | null;
}

export interface CuotaExoneracionRequest {
  motivoExoneracion: string;
}

export interface EstadoCuenta {
  idPuesto: number | null;
  idSocio: number | null;
  cantidadCuotasPendientes: number;
  totalPendiente: number;
  cuotas: Cuota[];
}

export interface Pago {
  idPago: number;
  idCuota: number;
  idTurno: number;
  idUsuarioCobro: number;
  metodoPago: MetodoPago;
  numeroOperacion: string | null;
  montoPagado: number;
  fechaPago: string;
  estadoPago: EstadoPago;
  motivoExtorno: string | null;
  fechaExtorno: string | null;
  idUsuarioExtorno: number | null;
  comprobante: Comprobante | null;
}

export interface PagoRequest {
  idCuota: number | null;
  metodoPago: MetodoPago;
  numeroOperacion: string | null;
}

export interface PagoExtornoRequest {
  motivoExtorno: string;
}

export interface Comprobante {
  idComprobante: number;
  idPago: number;
  idCuota: number;
  numeroComprobante: string;
  fechaEmision: string;
  montoTotal: number;
  metodoPago: MetodoPago;
  estadoComprobante: EstadoComprobante;
}

export interface Turno {
  idTurno: number;
  idUsuario: number;
  fechaApertura: string;
  fechaCierre: string | null;
  montoInicial: number;
  montoRecaudado: number;
  montoEsperado: number;
  diferencia: number;
  estadoTurno: EstadoTurno;
  observacionApertura: string | null;
  observacionCierre: string | null;
}

export interface TurnoAperturaRequest {
  montoInicial: number;
  observacionApertura: string | null;
}

export interface TurnoCierreRequest {
  montoRecaudado: number;
  observacionCierre: string | null;
}

export interface DeudaPendienteReporte {
  idCuota: number;
  idPuesto: number;
  idContrato: number;
  idConcepto: number;
  periodoMes: number;
  periodoAnio: number;
  montoTotal: number;
  fechaVencimiento: string | null;
}

export interface FlujoCajaReporte {
  idPago: number;
  idTurno: number;
  idCuota: number;
  idUsuarioCobro: number;
  metodoPago: MetodoPago;
  montoPagado: number;
  fechaPago: string;
}
