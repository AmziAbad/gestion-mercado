import { Injectable, computed, signal } from '@angular/core';

import { LoginResponse, RolUsuario } from '../models/auth.models';

@Injectable({
  providedIn: 'root',
})
export class Session {
  private readonly storageKey = 'gestion-mercado.session';
  private readonly currentSession = signal<LoginResponse | null>(this.restore());

  readonly user = computed(() => this.activeSession());

  token(): string | null {
    const session = this.activeSession();
    this.clearExpiredSession();
    return session?.token ?? null;
  }

  role(): RolUsuario | null {
    return this.activeSession()?.rol ?? null;
  }

  isAuthenticated(): boolean {
    const authenticated = Boolean(this.activeSession());
    this.clearExpiredSession();
    return authenticated;
  }

  hasAnyRole(roles: readonly RolUsuario[] | undefined): boolean {
    const currentRole = this.role();
    return Boolean(currentRole && (!roles?.length || roles.includes(currentRole)));
  }

  save(session: LoginResponse): void {
    this.currentSession.set(session);
    localStorage.setItem(this.storageKey, JSON.stringify(session));
  }

  clear(): void {
    this.currentSession.set(null);
    localStorage.removeItem(this.storageKey);
  }

  private restore(): LoginResponse | null {
    const raw = localStorage.getItem(this.storageKey);

    if (!raw) {
      return null;
    }

    try {
      const session = JSON.parse(raw) as LoginResponse;
      if (!session.token || this.isTokenExpired(session.token)) {
        localStorage.removeItem(this.storageKey);
        return null;
      }
      return session;
    } catch {
      localStorage.removeItem(this.storageKey);
      return null;
    }
  }

  private activeSession(): LoginResponse | null {
    const session = this.currentSession();
    if (!session?.token || this.isTokenExpired(session.token)) {
      return null;
    }
    return session;
  }

  private clearExpiredSession(): void {
    const session = this.currentSession();
    if (session?.token && this.isTokenExpired(session.token)) {
      this.clear();
    }
  }

  private isTokenExpired(token: string): boolean {
    const payload = token.split('.')[1];
    if (!payload) {
      return true;
    }

    try {
      const normalizedPayload = payload
        .replace(/-/g, '+')
        .replace(/_/g, '/')
        .padEnd(Math.ceil(payload.length / 4) * 4, '=');
      const bytes = Uint8Array.from(atob(normalizedPayload), (char) => char.charCodeAt(0));
      const claims = JSON.parse(new TextDecoder().decode(bytes)) as { exp?: number };
      return !claims.exp || claims.exp * 1000 <= Date.now();
    } catch {
      return true;
    }
  }
}
