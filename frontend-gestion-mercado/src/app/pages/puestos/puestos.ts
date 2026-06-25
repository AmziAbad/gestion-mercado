import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GestionApi, Puesto } from '../../core/gestion-api';

@Component({
  selector: 'app-puestos',
  imports: [FormsModule],
  templateUrl: './puestos.html',
  styleUrl: './puestos.css',
})
export class Puestos implements OnInit {
  puestos = signal<Puesto[]>([]);
  cargando = signal(true);
  procesando = signal(false);
  error = signal<string | null>(null);
  mensaje = signal<string | null>(null);
  modalAbierto = signal(false);
  pabellonFiltro = '';
  puestoForm: Puesto = this.nuevoPuesto();

  total = computed(() => this.puestos().length);
  ocupados = computed(
    () => this.puestos().filter((puesto) => puesto.estadoPuesto === 'OCUPADO').length,
  );
  asociacion = computed(() => this.puestos().filter((puesto) => puesto.idSocioActual === 1).length);
  mantenimiento = computed(
    () => this.puestos().filter((puesto) => puesto.estadoPuesto === 'MANTENIMIENTO').length,
  );

  constructor(private readonly api: GestionApi) {}

  ngOnInit(): void {
    this.cargarPuestos();
  }

  cargarPuestos(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.api.listarPuestos().subscribe({
      next: (puestos) => {
        this.puestos.set(puestos);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar puestos desde el backend.');
        this.cargando.set(false);
      },
    });
  }

  filtrarPabellon(): void {
    const pabellon = this.pabellonFiltro.trim();
    if (!pabellon) {
      this.cargarPuestos();
      return;
    }

    this.cargando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.listarPuestosPorPabellon(pabellon).subscribe({
      next: (puestos) => {
        this.puestos.set(puestos);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo filtrar puestos por pabellon.');
        this.cargando.set(false);
      },
    });
  }

  prepararNuevo(): void {
    this.puestoForm = this.nuevoPuesto();
    this.mensaje.set('Formulario listo para registrar puesto.');
    this.error.set(null);
    this.modalAbierto.set(true);
  }

  editar(puesto: Puesto): void {
    this.puestoForm = { ...puesto };
    this.mensaje.set(`Editando puesto ${puesto.numeroPuesto}.`);
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

    this.api.guardarPuesto(this.puestoForm).subscribe({
      next: (puesto) => {
        this.puestoForm = { ...puesto };
        this.mensaje.set('Puesto guardado correctamente.');
        this.modalAbierto.set(false);
        this.procesando.set(false);
        this.cargarPuestos();
      },
      error: () => {
        this.error.set('No se pudo guardar el puesto. Verifica codigo, pabellon y precio.');
        this.procesando.set(false);
      },
    });
  }

  eliminar(puesto: Puesto): void {
    if (!puesto.idPuesto) {
      return;
    }

    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.eliminarPuesto(puesto.idPuesto).subscribe({
      next: () => {
        this.mensaje.set('Puesto eliminado correctamente.');
        this.procesando.set(false);
        this.cargarPuestos();
      },
      error: () => {
        this.error.set('No se pudo eliminar el puesto.');
        this.procesando.set(false);
      },
    });
  }

  private nuevoPuesto(): Puesto {
    return {
      numeroPuesto: '',
      pabellon: '',
      medidas: '2x2m',
      precio: 0,
      estadoPuesto: 'VACANTE',
      idSocioActual: undefined,
    };
  }
}
