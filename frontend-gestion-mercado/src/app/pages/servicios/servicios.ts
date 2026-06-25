import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GestionApi, Servicio } from '../../core/gestion-api';

@Component({
  selector: 'app-servicios',
  imports: [FormsModule],
  templateUrl: './servicios.html',
  styleUrl: './servicios.css',
})
export class Servicios implements OnInit {
  servicios = signal<Servicio[]>([]);
  cargando = signal(true);
  procesando = signal(false);
  error = signal<string | null>(null);
  mensaje = signal<string | null>(null);
  modalAbierto = signal(false);
  mostrarInactivos = true;
  servicioForm: Servicio = this.nuevoServicio();

  constructor(private readonly api: GestionApi) {}

  ngOnInit(): void {
    this.cargarServicios();
  }

  cargarServicios(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.api.listarServicios().subscribe({
      next: (servicios) => {
        this.servicios.set(servicios);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar servicios desde el backend.');
        this.cargando.set(false);
      },
    });
  }

  verInactivos(): void {
    this.mostrarInactivos = !this.mostrarInactivos;
    this.cargando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    const request = this.mostrarInactivos
      ? this.api.listarServicios()
      : this.api.listarServiciosActivos();
    request.subscribe({
      next: (servicios) => {
        this.servicios.set(servicios);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar el catalogo de servicios.');
        this.cargando.set(false);
      },
    });
  }

  prepararNuevo(): void {
    this.servicioForm = this.nuevoServicio();
    this.mensaje.set('Formulario listo para registrar concepto.');
    this.error.set(null);
    this.modalAbierto.set(true);
  }

  editar(servicio: Servicio): void {
    this.servicioForm = { ...servicio };
    this.mensaje.set(`Editando concepto ${servicio.nombreServicio}.`);
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

    this.api.guardarServicio(this.servicioForm).subscribe({
      next: (servicio) => {
        this.servicioForm = { ...servicio };
        this.mensaje.set('Concepto guardado correctamente.');
        this.modalAbierto.set(false);
        this.procesando.set(false);
        this.cargarServicios();
      },
      error: () => {
        this.error.set('No se pudo guardar el concepto. Verifica nombre, tipo y monto.');
        this.procesando.set(false);
      },
    });
  }

  cambiarEstado(servicio: Servicio): void {
    if (!servicio.idServicio) {
      return;
    }

    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    const request = servicio.activo
      ? this.api.desactivarServicio(servicio.idServicio)
      : this.api.activarServicio(servicio.idServicio);

    request.subscribe({
      next: () => {
        this.mensaje.set('Estado del concepto actualizado.');
        this.procesando.set(false);
        this.cargarServicios();
      },
      error: () => {
        this.error.set('No se pudo actualizar el estado del concepto.');
        this.procesando.set(false);
      },
    });
  }

  eliminar(servicio: Servicio): void {
    if (!servicio.idServicio) {
      return;
    }

    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.eliminarServicio(servicio.idServicio).subscribe({
      next: () => {
        this.mensaje.set('Concepto eliminado correctamente.');
        this.procesando.set(false);
        this.cargarServicios();
      },
      error: () => {
        this.error.set('No se pudo eliminar el concepto.');
        this.procesando.set(false);
      },
    });
  }

  monto(servicio: Servicio): string {
    const valor =
      servicio.tipoCobro === 'FIJO' ? servicio.montoFijoPuesto : servicio.costoTotalExterno;
    return `S/ ${(valor ?? 0).toFixed(2)}`;
  }

  private nuevoServicio(): Servicio {
    return {
      nombre: '',
      nombreServicio: '',
      tipoCobro: 'FIJO',
      montoFijoPuesto: 0,
      costoTotalExterno: 0,
      activo: true,
    };
  }
}
