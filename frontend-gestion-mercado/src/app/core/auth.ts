import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  mensaje: string;
  token: string;
  username: string;
  rol: string;
  activo: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly loginUrl = '/api-UsuarioLogin-service/usuarios/login';
  private readonly tokenKey = 'gestionMercadoToken';
  private readonly userKey = 'gestionMercadoUser';

  constructor(private readonly http: HttpClient) {}

  login(request: LoginRequest) {
    return this.http.post<LoginResponse>(this.loginUrl, request).pipe(
      tap((response) => {
        localStorage.setItem(this.tokenKey, response.token);
        localStorage.setItem(this.userKey, JSON.stringify(response));
      }),
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }

  estaAutenticado(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  usuarioActual(): LoginResponse | null {
    const value = localStorage.getItem(this.userKey);
    return value ? (JSON.parse(value) as LoginResponse) : null;
  }
}
