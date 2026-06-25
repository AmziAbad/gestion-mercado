import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GestionApi, Usuario } from '../../core/gestion-api';

@Component({
  selector: 'app-usuarios',
  imports: [FormsModule],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css',
})
export class Usuarios implements OnInit {
  usuarios = signal<Usuario[]>([]);
  cargando = signal(true);
  procesando = signal(false);
  error = signal<string | null>(null);
  mensaje = signal<string | null>(null);
  modalAbierto = signal(false);
  usuarioForm: Usuario = this.nuevoUsuario();

  usuariosActivos = computed(() => this.usuarios().filter((usuario) => usuario.activo).length);

  constructor(private readonly api: GestionApi) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.api.listarUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios.set(usuarios);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.error.set(
          'No se pudo cargar usuarios. Inicia sesion y verifica que el Gateway este activo.',
        );
      },
    });
  }

  prepararNuevo(): void {
    this.usuarioForm = this.nuevoUsuario();
    this.mensaje.set('Formulario listo para registrar usuario.');
    this.error.set(null);
    this.modalAbierto.set(true);
  }

  editar(usuario: Usuario): void {
    this.usuarioForm = { ...usuario, password: '' };
    this.mensaje.set(`Editando usuario ${usuario.username}.`);
    this.error.set(null);
    this.modalAbierto.set(true);
  }

  cerrarModal(): void {
    this.modalAbierto.set(false);
  }

  guardar(): void {
    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.guardarUsuario(this.usuarioForm).subscribe({
      next: (usuario) => {
        this.usuarioForm = { ...usuario, password: '' };
        this.mensaje.set('Usuario guardado correctamente.');
        this.modalAbierto.set(false);
        this.procesando.set(false);
        this.cargarUsuarios();
      },
      error: () => {
        this.error.set('No se pudo guardar el usuario. Verifica username, DNI, rol y clave.');
        this.procesando.set(false);
      },
    });
  }

  eliminar(usuario: Usuario): void {
    if (!usuario.idUsuario) {
      return;
    }

    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.eliminarUsuario(usuario.idUsuario).subscribe({
      next: () => {
        this.mensaje.set('Usuario desactivado correctamente.');
        this.procesando.set(false);
        this.cargarUsuarios();
      },
      error: () => {
        this.error.set('No se pudo desactivar el usuario.');
        this.procesando.set(false);
      },
    });
  }

  restablecerClave(): void {
    if (!this.usuarioForm.idUsuario) {
      this.error.set('Selecciona un usuario de la tabla para restablecer su clave.');
      return;
    }

    const nuevaClave = window.prompt('Nueva clave temporal');
    if (!nuevaClave) {
      return;
    }

    this.usuarioForm = { ...this.usuarioForm, password: nuevaClave };
    this.guardar();
  }

  private nuevoUsuario(): Usuario {
    return {
      username: '',
      password: '',
      nombreCompleto: '',
      dni: '',
      correo: '',
      telefono: '',
      rol: 'ADMIN',
      activo: true,
    };
  }
}
