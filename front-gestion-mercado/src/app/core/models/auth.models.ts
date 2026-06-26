export type RolUsuario = 'ADMIN' | 'TESORERO' | 'RECEPCIONISTA';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  idUsuario: number;
  username: string;
  nombreCompleto: string;
  rol: RolUsuario;
  token: string;
  expirationSeconds: number;
}

export interface Usuario {
  idUsuario: number;
  username: string;
  nombreCompleto: string;
  dni: string;
  correo: string | null;
  telefono: string | null;
  rol: RolUsuario;
  activo: boolean;
  fechaRegistro: string;
}

export interface UsuarioCrearRequest {
  username: string;
  password: string;
  nombreCompleto: string;
  dni: string;
  correo: string | null;
  telefono: string | null;
  rol: RolUsuario;
}

export interface UsuarioActualizarRequest {
  nombreCompleto: string;
  dni: string;
  correo: string | null;
  telefono: string | null;
  rol: RolUsuario;
  activo: boolean;
}

export interface UsuarioEstadoRequest {
  activo: boolean;
}

export interface UsuarioPasswordRequest {
  password: string;
}
