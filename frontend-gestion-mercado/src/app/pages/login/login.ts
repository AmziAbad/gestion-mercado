import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../core/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  username = 'admin';
  password = 'admin123';
  cargando = signal(false);
  error = signal<string | null>(null);

  constructor(
    private readonly auth: Auth,
    private readonly router: Router,
  ) {}

  ingresar(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.auth.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.cargando.set(false);
        this.error.set(
          'No se pudo iniciar sesion. Verifica que el Gateway y UsuarioLogin esten activos.',
        );
      },
    });
  }
}
