import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { catchError, forkJoin, of } from 'rxjs';
import { EstadoDeudores, FlujoCajaDiario, GestionApi, Puesto, Socio } from '../../core/gestion-api';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  socios = signal<Socio[]>([]);
  puestos = signal<Puesto[]>([]);
  deudores = signal<EstadoDeudores | null>(null);
  flujoCaja = signal<FlujoCajaDiario | null>(null);
  cargando = signal(true);
  error = signal<string | null>(null);

  sociosActivos = computed(() => this.socios().filter((socio) => socio.activo).length);
  puestosOcupados = computed(
    () => this.puestos().filter((puesto) => puesto.estadoPuesto === 'OCUPADO').length,
  );
  puestosAsociacion = computed(
    () => this.puestos().filter((puesto) => puesto.idSocioActual === 1).length,
  );
  deudaPendiente = computed(() => this.deudores()?.totalDeuda ?? 0);
  ingresosHoy = computed(() => this.flujoCaja()?.totalRecaudado ?? 0);
  pagosHoy = computed(() => this.flujoCaja()?.totalPagos ?? 0);

  constructor(private readonly api: GestionApi) {}

  ngOnInit(): void {
    this.cargarDashboard();
  }

  cargarDashboard(): void {
    this.cargando.set(true);
    this.error.set(null);

    const hoy = new Date().toISOString().slice(0, 10);

    forkJoin({
      socios: this.api.listarSocios().pipe(catchError(() => of([] as Socio[]))),
      puestos: this.api.listarPuestos().pipe(catchError(() => of([] as Puesto[]))),
      deudores: this.api.obtenerDeudores().pipe(catchError(() => of(null))),
      flujoCaja: this.api.obtenerFlujoCaja(hoy).pipe(catchError(() => of(null))),
    }).subscribe({
      next: ({ socios, puestos, deudores, flujoCaja }) => {
        this.socios.set(socios);
        this.puestos.set(puestos);
        this.deudores.set(deudores);
        this.flujoCaja.set(flujoCaja);
        this.cargando.set(false);

        if (!socios.length && !puestos.length && !deudores && !flujoCaja) {
          this.error.set(
            'No se pudieron cargar datos desde el Gateway. Revisa token y microservicios activos.',
          );
        }
      },
    });
  }

  formatoMoneda(valor: number | null | undefined): string {
    return `S/ ${(valor ?? 0).toLocaleString('es-PE', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })}`;
  }
}
