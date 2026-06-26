import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { TableActionEvent, TableColumn } from '../../../../core/models/table.models';
import {
  RolUsuario,
  Usuario,
  UsuarioActualizarRequest,
  UsuarioCrearRequest,
} from '../../../../core/models/auth.models';
import { AuthApi } from '../../../../core/services/auth-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-usuarios',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css',
})
export class Usuarios {
  readonly columns: TableColumn[] = [
    { key: 'idUsuario', label: 'ID' },
    { key: 'username', label: 'Usuario' },
    { key: 'nombreCompleto', label: 'Nombre' },
    { key: 'dni', label: 'DNI' },
    { key: 'rol', label: 'Rol', type: 'status' },
    { key: 'activo', label: 'Activo', type: 'boolean' },
  ];

  readonly actions = [
    { id: 'edit', label: 'Editar', tone: 'secondary' as const },
    { id: 'toggle', label: 'Activar/Desactivar', tone: 'secondary' as const },
  ];

  usuarios = signal<Usuario[]>([]);
  form = this.emptyForm();
  editingId: number | null = null;
  passwordReset = {
    idUsuario: null as number | null,
    password: '',
  };
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly authApi: AuthApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.authApi.listarUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios.set(usuarios);
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
        this.loading.set(false);
      },
      complete: () => {
        this.loading.set(false);
      },
    });
  }

  save(): void {
    this.message.set('');
    this.error.set('');

    const operation =
      this.editingId === null
        ? this.authApi.crearUsuario(this.createRequest())
        : this.authApi.actualizarUsuario(this.editingId, this.updateRequest());

    operation.subscribe({
      next: () => {
        this.message.set(this.editingId === null ? 'Usuario registrado.' : 'Usuario actualizado.');
        this.reset();
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  handleAction(event: TableActionEvent): void {
    const usuario = event.row as Usuario;

    if (event.action.id === 'edit') {
      this.editingId = usuario.idUsuario;
      this.form = {
        username: usuario.username,
        password: '',
        nombreCompleto: usuario.nombreCompleto,
        dni: usuario.dni,
        correo: usuario.correo,
        telefono: usuario.telefono,
        rol: usuario.rol,
        activo: usuario.activo,
      };
      this.passwordReset.idUsuario = usuario.idUsuario;
      return;
    }

    this.authApi.cambiarEstadoUsuario(usuario.idUsuario, { activo: !usuario.activo }).subscribe({
      next: () => {
        this.message.set('Estado de usuario actualizado.');
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  cambiarPassword(): void {
    this.message.set('');
    this.error.set('');

    if (this.passwordReset.idUsuario === null || !this.passwordReset.password.trim()) {
      this.error.set('Indica usuario y nueva contrasena.');
      return;
    }

    this.authApi
      .cambiarPassword(this.passwordReset.idUsuario, { password: this.passwordReset.password })
      .subscribe({
        next: () => {
          this.message.set('Contrasena actualizada.');
          this.passwordReset = { idUsuario: null, password: '' };
        },
        error: (error: unknown) => {
          this.error.set(httpErrorMessage(error));
        },
      });
  }

  reset(): void {
    this.editingId = null;
    this.form = this.emptyForm();
  }

  private createRequest(): UsuarioCrearRequest {
    return {
      username: this.form.username,
      password: this.form.password,
      nombreCompleto: this.form.nombreCompleto,
      dni: this.form.dni,
      correo: this.form.correo,
      telefono: this.form.telefono,
      rol: this.form.rol,
    };
  }

  private updateRequest(): UsuarioActualizarRequest {
    return {
      nombreCompleto: this.form.nombreCompleto,
      dni: this.form.dni,
      correo: this.form.correo,
      telefono: this.form.telefono,
      rol: this.form.rol,
      activo: this.form.activo,
    };
  }

  private emptyForm(): UsuarioCrearRequest & { activo: boolean } {
    return {
      username: '',
      password: '',
      nombreCompleto: '',
      dni: '',
      correo: null,
      telefono: null,
      rol: 'ADMIN' as RolUsuario,
      activo: true,
    };
  }
}
