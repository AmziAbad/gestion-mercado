import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Comprobante, CuotaPago, GestionApi } from '../../core/gestion-api';

@Component({
  selector: 'app-cobranzas',
  imports: [FormsModule],
  templateUrl: './cobranzas.html',
  styleUrl: './cobranzas.css',
})
export class Cobranzas implements OnInit {
  cuotas = signal<CuotaPago[]>([]);
  cargando = signal(true);
  procesando = signal(false);
  error = signal<string | null>(null);
  mensaje = signal<string | null>(null);
  modalActivo = signal<'generar' | 'pago' | 'comprobante' | null>(null);
  comprobante = signal<Comprobante | null>(null);

  mesGeneracion = new Date().getMonth() + 1;
  anioGeneracion = new Date().getFullYear();
  idCuotaSeleccionada: number | null = null;
  metodoPago = 'EFECTIVO';
  numeroOperacion = '';

  pendientes = computed(() => this.cuotas().filter((cuota) => cuota.estado === 'PENDIENTE'));
  pagadas = computed(() => this.cuotas().filter((cuota) => cuota.estado === 'PAGADO'));
  totalPendiente = computed(() =>
    this.pendientes().reduce((total, cuota) => total + (cuota.monto ?? 0), 0),
  );

  constructor(private readonly api: GestionApi) {}

  ngOnInit(): void {
    this.cargarCuotas();
  }

  cargarCuotas(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.api.listarCuotas().subscribe({
      next: (cuotas) => {
        this.cuotas.set(cuotas);
        this.idCuotaSeleccionada = this.pendientes()[0]?.idCuota ?? null;
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar cuotas desde el backend.');
        this.cargando.set(false);
      },
    });
  }

  generarCuotas(): void {
    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.generarCuotas(this.mesGeneracion, this.anioGeneracion).subscribe({
      next: (respuesta) => {
        this.mensaje.set(respuesta.mensaje);
        this.modalActivo.set(null);
        this.procesando.set(false);
        this.cargarCuotas();
      },
      error: () => {
        this.error.set(
          'No se pudo generar cuotas. Verifica servicios, puestos ocupados y periodo.',
        );
        this.procesando.set(false);
      },
    });
  }

  registrarPago(): void {
    if (!this.idCuotaSeleccionada) {
      this.error.set('Selecciona una cuota pendiente para registrar el pago.');
      return;
    }

    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api
      .pagarCuota(this.idCuotaSeleccionada, {
        metodoPago: this.metodoPago,
        numeroOperacion: this.numeroOperacion.trim() || undefined,
      })
      .subscribe({
        next: (cuota) => {
          this.mensaje.set(
            `Pago registrado. Comprobante ${cuota.numeroComprobante || 'generado'}.`,
          );
          this.numeroOperacion = '';
          this.modalActivo.set(null);
          this.procesando.set(false);
          this.cargarCuotas();
        },
        error: () => {
          this.error.set('No se pudo registrar el pago de la cuota seleccionada.');
          this.procesando.set(false);
        },
      });
  }

  abrirModalGenerar(): void {
    this.modalActivo.set('generar');
    this.error.set(null);
    this.mensaje.set(null);
  }

  abrirModalPago(idCuota?: number): void {
    if (idCuota) {
      this.idCuotaSeleccionada = idCuota;
    }
    this.modalActivo.set('pago');
    this.error.set(null);
    this.mensaje.set(null);
  }

  cerrarModal(): void {
    this.modalActivo.set(null);
  }

  verComprobante(idCuota: number): void {
    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);
    this.comprobante.set(null);

    this.api.generarComprobante(idCuota).subscribe({
      next: (comprobante) => {
        this.comprobante.set(comprobante);
        this.modalActivo.set('comprobante');
        this.procesando.set(false);
      },
      error: () => {
        this.error.set('No se pudo obtener el comprobante de la cuota.');
        this.procesando.set(false);
      },
    });
  }

  formatoMoneda(valor: number | null | undefined): string {
    return `S/ ${(valor ?? 0).toFixed(2)}`;
  }
}
