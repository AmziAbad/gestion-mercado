import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { catchError, forkJoin, of } from 'rxjs';
import { EstadoDeudores, FlujoCajaDiario, GestionApi } from '../../core/gestion-api';

@Component({
  selector: 'app-reportes',
  imports: [FormsModule],
  templateUrl: './reportes.html',
  styleUrl: './reportes.css',
})
export class Reportes implements OnInit {
  flujoCaja = signal<FlujoCajaDiario | null>(null);
  deudores = signal<EstadoDeudores | null>(null);
  cargando = signal(true);
  procesando = signal(false);
  error = signal<string | null>(null);
  mensaje = signal<string | null>(null);
  fechaReporte = new Date().toISOString().slice(0, 10);
  tipoReporte = 'flujo-caja';

  totalRecaudado = computed(() => this.flujoCaja()?.totalRecaudado ?? 0);
  totalPagos = computed(() => this.flujoCaja()?.totalPagos ?? 0);
  totalDeudores = computed(() => this.deudores()?.totalPuestosConDeuda ?? 0);
  totalDeuda = computed(() => this.deudores()?.totalDeuda ?? 0);

  constructor(private readonly api: GestionApi) {}

  ngOnInit(): void {
    this.cargarReportes();
  }

  cargarReportes(): void {
    this.cargando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    forkJoin({
      flujoCaja: this.api.obtenerFlujoCaja(this.fechaReporte).pipe(catchError(() => of(null))),
      deudores: this.api.obtenerDeudores().pipe(catchError(() => of(null))),
    }).subscribe(({ flujoCaja, deudores }) => {
      this.flujoCaja.set(flujoCaja);
      this.deudores.set(deudores);
      this.cargando.set(false);
      if (!flujoCaja && !deudores) {
        this.error.set('No se pudieron cargar reportes desde el backend.');
      } else {
        this.mensaje.set('Vista previa actualizada.');
      }
    });
  }

  exportarPdf(): void {
    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    const request =
      this.tipoReporte === 'deudores'
        ? this.api.descargarDeudoresPdf()
        : this.api.descargarFlujoCajaPdf(this.fechaReporte);

    request.subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        window.open(url, '_blank');
        this.mensaje.set('PDF generado correctamente.');
        this.procesando.set(false);
      },
      error: () => {
        this.error.set('No se pudo exportar el PDF del reporte.');
        this.procesando.set(false);
      },
    });
  }

  formatoMoneda(valor: number | null | undefined): string {
    return `S/ ${Number(valor ?? 0).toFixed(2)}`;
  }

  formatoHora(valor: string | undefined): string {
    return valor
      ? new Date(valor).toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit' })
      : '-';
  }
}
