import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GestionApi, Socio } from '../../core/gestion-api';

@Component({
  selector: 'app-socios',
  imports: [FormsModule],
  templateUrl: './socios.html',
  styleUrl: './socios.css',
})
export class Socios implements OnInit {
  socios = signal<Socio[]>([]);
  cargando = signal(true);
  procesando = signal(false);
  error = signal<string | null>(null);
  mensaje = signal<string | null>(null);
  modalAbierto = signal(false);
  dniBusqueda = '';
  socioForm: Socio = this.nuevoSocio();

  constructor(private readonly api: GestionApi) {}

  ngOnInit(): void {
    this.cargarSocios();
  }

  cargarSocios(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.api.listarSocios().subscribe({
      next: (socios) => {
        this.socios.set(socios);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar socios desde el backend.');
        this.cargando.set(false);
      },
    });
  }

  buscarDni(): void {
    const dni = this.dniBusqueda.trim();
    if (!dni) {
      this.cargarSocios();
      return;
    }

    this.cargando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.buscarSocioPorDni(dni).subscribe({
      next: (socio) => {
        this.socios.set([socio]);
        this.socioForm = { ...socio };
        this.cargando.set(false);
      },
      error: () => {
        this.socios.set([]);
        this.error.set('No se encontro un socio con ese DNI.');
        this.cargando.set(false);
      },
    });
  }

  prepararNuevo(): void {
    this.socioForm = this.nuevoSocio();
    this.dniBusqueda = '';
    this.mensaje.set('Formulario listo para registrar socio.');
    this.error.set(null);
    this.modalAbierto.set(true);
  }

  editar(socio: Socio): void {
    this.socioForm = { ...socio };
    this.mensaje.set(`Editando socio ${socio.dni}.`);
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

    this.api.guardarSocio(this.socioForm).subscribe({
      next: (socio) => {
        this.socioForm = { ...socio };
        this.mensaje.set('Socio guardado correctamente.');
        this.modalAbierto.set(false);
        this.procesando.set(false);
        this.cargarSocios();
      },
      error: () => {
        this.error.set('No se pudo guardar el socio. Verifica DNI unico y campos requeridos.');
        this.procesando.set(false);
      },
    });
  }

  eliminar(socio: Socio): void {
    if (!socio.idSocio || socio.esAsociacion) {
      return;
    }

    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.eliminarSocio(socio.idSocio).subscribe({
      next: () => {
        this.mensaje.set('Socio eliminado correctamente.');
        this.procesando.set(false);
        this.cargarSocios();
      },
      error: () => {
        this.error.set('No se pudo eliminar el socio.');
        this.procesando.set(false);
      },
    });
  }

  private nuevoSocio(): Socio {
    return {
      dni: '',
      nombre: '',
      apellido: '',
      telefono: '',
      correo: '',
      direccion: '',
      estadoSolvencia: true,
      activo: true,
      esAsociacion: false,
    };
  }
}
