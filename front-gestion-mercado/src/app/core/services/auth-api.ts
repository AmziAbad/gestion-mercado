import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

import { API_ENDPOINTS } from '../constants/api-endpoints';
import {
  LoginRequest,
  LoginResponse,
  Usuario,
  UsuarioActualizarRequest,
  UsuarioCrearRequest,
  UsuarioEstadoRequest,
  UsuarioPasswordRequest,
} from '../models/auth.models';
import { Session } from './session';

@Injectable({
  providedIn: 'root',
})
export class AuthApi {
  constructor(
    private readonly http: HttpClient,
    private readonly session: Session,
  ) {}

  login(request: LoginRequest) {
    return this.http
      .post<LoginResponse>(`${API_ENDPOINTS.auth}/login`, request)
      .pipe(tap((response) => this.session.save(response)));
  }

  logout(): void {
    this.session.clear();
  }

  listarUsuarios() {
    return this.http.get<Usuario[]>(API_ENDPOINTS.usuarios);
  }

  obtenerUsuario(idUsuario: number) {
    return this.http.get<Usuario>(`${API_ENDPOINTS.usuarios}/${idUsuario}`);
  }

  crearUsuario(request: UsuarioCrearRequest) {
    return this.http.post<Usuario>(API_ENDPOINTS.usuarios, request);
  }

  actualizarUsuario(idUsuario: number, request: UsuarioActualizarRequest) {
    return this.http.put<Usuario>(`${API_ENDPOINTS.usuarios}/${idUsuario}`, request);
  }

  cambiarEstadoUsuario(idUsuario: number, request: UsuarioEstadoRequest) {
    return this.http.patch<Usuario>(`${API_ENDPOINTS.usuarios}/${idUsuario}/estado`, request);
  }

  cambiarPassword(idUsuario: number, request: UsuarioPasswordRequest) {
    return this.http.put<void>(`${API_ENDPOINTS.usuarios}/${idUsuario}/password`, request);
  }
}
