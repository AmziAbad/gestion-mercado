import { Component, computed, signal } from '@angular/core';
import { catchError, forkJoin, of } from 'rxjs';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { SummaryCard } from '../../../../shared/components/summary-card/summary-card';
import { AppIcon } from '../../../../shared/components/app-icon/app-icon';
import { PatrimonioApi } from '../../../../core/services/patrimonio-api';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { AuditoriaReportesApi } from '../../../../core/services/auditoria-reportes-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';
import { Session } from '../../../../core/services/session';
import { ROLE_GROUPS } from '../../../../core/constants/roles';
import { RolUsuario } from '../../../../core/models/auth.models';
import { Puesto, Transferencia } from '../../../../core/models/patrimonio.models';
import {
  Cuota,
  DeudaPendienteReporte,
  FlujoCajaReporte,
  MetodoPago,
  Turno,
} from '../../../../core/models/tesoreria.models';
import { AuditoriaAnulacion, AuditoriaEvento } from '../../../../core/models/auditoria.models';

interface DashboardSummary {
  socios: number;
  puestos: number;
  puestosLibres: number;
  puestosOcupados: number;
  contratosActivos: number;
  transferencias: number;
  cuotasPendientes: number;
  cuotasVencidas: number;
  totalPendiente: number;
  recaudadoHoy: number;
  pagosHoy: number;
  turnosAbiertos: number;
  eventosAuditoria: number;
  anulaciones: number;
}

interface DashboardMetric {
  key: keyof DashboardSummary;
  label: string;
  helper: string;
  icon: string;
  roles: readonly RolUsuario[];
  currency?: boolean;
}

interface OccupancySnapshot {
  total: number;
  ocupados: number;
  libres: number;
  mantenimiento: number;
  porcentajeOcupacion: number;
}

interface BarItem {
  label: string;
  value: number;
  percent: number;
  formatted: string;
}

interface DebtRow {
  idPuesto: number;
  idCuota: number;
  periodo: string;
  monto: number;
}

interface ActivityRow {
  label: string;
  detail: string;
  date: string;
  type: 'transferencia' | 'evento' | 'anulacion';
}

@Component({
  selector: 'app-dashboard',
  imports: [PageHeader, SummaryCard, AppIcon],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  loading = signal(false);
  error = signal('');

  summary = signal<DashboardSummary>({
    socios: 0,
    puestos: 0,
    puestosLibres: 0,
    puestosOcupados: 0,
    contratosActivos: 0,
    transferencias: 0,
    cuotasPendientes: 0,
    cuotasVencidas: 0,
    totalPendiente: 0,
    recaudadoHoy: 0,
    pagosHoy: 0,
    turnosAbiertos: 0,
    eventosAuditoria: 0,
    anulaciones: 0,
  });

  occupancy = signal<OccupancySnapshot>({
    total: 0,
    ocupados: 0,
    libres: 0,
    mantenimiento: 0,
    porcentajeOcupacion: 0,
  });
  paymentMethods = signal<BarItem[]>([]);
  quotaStatus = signal<BarItem[]>([]);
  topDebts = signal<DebtRow[]>([]);
  recentActivity = signal<ActivityRow[]>([]);
  activeTurn = signal<Turno | null>(null);

  readonly canViewPatrimonio = computed(() => this.session.hasAnyRole(ROLE_GROUPS.patrimonio));
  readonly canViewTesoreria = computed(() => this.session.hasAnyRole(ROLE_GROUPS.tesoreria));
  readonly canViewAuditoria = computed(() =>
    this.session.hasAnyRole(ROLE_GROUPS.auditoriaReportes),
  );

  readonly metricDefinitions: DashboardMetric[] = [
    {
      key: 'socios',
      label: 'Socios registrados',
      helper: 'Padron administrativo',
      icon: 'socios',
      roles: ROLE_GROUPS.patrimonio,
    },
    {
      key: 'puestosOcupados',
      label: 'Puestos ocupados',
      helper: 'Con contrato activo',
      icon: 'puestos',
      roles: ROLE_GROUPS.patrimonio,
    },
    {
      key: 'totalPendiente',
      label: 'Deuda pendiente',
      helper: 'Monto por cobrar',
      icon: 'cuotas',
      roles: ROLE_GROUPS.tesoreria,
      currency: true,
    },
    {
      key: 'recaudadoHoy',
      label: 'Cobrado hoy',
      helper: 'Ingresos del dia',
      icon: 'pagos',
      roles: ROLE_GROUPS.tesoreria,
      currency: true,
    },
    {
      key: 'eventosAuditoria',
      label: 'Eventos de auditoria',
      helper: 'Trazabilidad',
      icon: 'eventos',
      roles: ROLE_GROUPS.auditoriaReportes,
    },
    {
      key: 'anulaciones',
      label: 'Anulaciones',
      helper: 'Sustentos registrados',
      icon: 'anulaciones',
      roles: ROLE_GROUPS.auditoriaReportes,
    },
  ];

  readonly visibleMetrics = computed(() =>
    this.metricDefinitions.filter((metric) => this.session.hasAnyRole(metric.roles)),
  );

  constructor(
    private readonly patrimonioApi: PatrimonioApi,
    private readonly tesoreriaApi: TesoreriaApi,
    private readonly auditoriaApi: AuditoriaReportesApi,
    private readonly session: Session,
  ) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    const puedeConsultarPatrimonio = this.canViewPatrimonio();
    const puedeConsultarTesoreria = this.canViewTesoreria();
    const puedeConsultarAuditoria = this.canViewAuditoria();
    const fechaHoy = this.todayIso();

    forkJoin({
      socios: puedeConsultarPatrimonio
        ? this.patrimonioApi.listarSocios().pipe(catchError(() => of([])))
        : of([]),
      puestos: puedeConsultarPatrimonio
        ? this.patrimonioApi.listarPuestos().pipe(catchError(() => of([])))
        : of([]),
      contratos: puedeConsultarPatrimonio
        ? this.patrimonioApi.listarContratosActivos().pipe(catchError(() => of([])))
        : of([]),
      transferencias: puedeConsultarPatrimonio
        ? this.patrimonioApi.listarTransferencias().pipe(catchError(() => of([])))
        : of([]),
      cuotas: puedeConsultarTesoreria
        ? this.tesoreriaApi.listarCuotas().pipe(catchError(() => of([])))
        : of([]),
      flujoHoy: puedeConsultarTesoreria
        ? this.tesoreriaApi.datosFlujoCaja(fechaHoy).pipe(catchError(() => of([])))
        : of([]),
      turnosHoy: puedeConsultarTesoreria
        ? this.tesoreriaApi.listarTurnos(fechaHoy).pipe(catchError(() => of([])))
        : of([]),
      morosidad: puedeConsultarTesoreria
        ? this.tesoreriaApi.datosMorosidad().pipe(catchError(() => of([])))
        : of([]),
      eventos: puedeConsultarAuditoria
        ? this.auditoriaApi.listarEventos().pipe(catchError(() => of([])))
        : of([]),
      anulaciones: puedeConsultarAuditoria
        ? this.auditoriaApi.listarAnulaciones().pipe(catchError(() => of([])))
        : of([]),
    }).subscribe({
      next: ({
        socios,
        puestos,
        contratos,
        transferencias,
        cuotas,
        flujoHoy,
        turnosHoy,
        morosidad,
        eventos,
        anulaciones,
      }) => {
        const cuotasPendientes = cuotas.filter((cuota) => cuota.estadoCuota === 'PENDIENTE');
        const cuotasVencidas = cuotasPendientes.filter((cuota) => this.isOverdue(cuota)).length;
        const totalPendiente = cuotasPendientes.reduce(
          (total, cuota) => total + cuota.montoTotal,
          0,
        );
        const recaudadoHoy = flujoHoy.reduce((total, pago) => total + pago.montoPagado, 0);
        const turnosAbiertos = turnosHoy.filter((turno) => turno.estadoTurno === 'ABIERTO');

        this.summary.set({
          socios: socios.length,
          puestos: puestos.length,
          puestosLibres: puestos.filter((puesto) => puesto.estadoPuesto === 'LIBRE').length,
          puestosOcupados: puestos.filter((puesto) => puesto.estadoPuesto === 'OCUPADO').length,
          contratosActivos: contratos.length,
          transferencias: transferencias.length,
          cuotasPendientes: cuotasPendientes.length,
          cuotasVencidas,
          totalPendiente,
          recaudadoHoy,
          pagosHoy: flujoHoy.length,
          turnosAbiertos: turnosAbiertos.length,
          eventosAuditoria: eventos.length,
          anulaciones: anulaciones.length,
        });

        this.occupancy.set(this.buildOccupancy(puestos));
        this.paymentMethods.set(this.buildPaymentMethods(flujoHoy));
        this.quotaStatus.set(this.buildQuotaStatus(cuotas));
        this.topDebts.set(this.buildTopDebts(morosidad));
        this.recentActivity.set(this.buildRecentActivity(transferencias, eventos, anulaciones));
        this.activeTurn.set(turnosAbiertos[0] ?? null);
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

  metricValue(metric: DashboardMetric): string | number {
    const value = this.summary()[metric.key];
    return metric.currency ? this.money(value) : value;
  }

  money(value: number): string {
    return `S/ ${value.toFixed(2)}`;
  }

  percent(value: number): string {
    return `${Math.round(value)}%`;
  }

  occupationBackground(): string {
    const snapshot = this.occupancy();
    const ocupado = snapshot.total ? (snapshot.ocupados / snapshot.total) * 100 : 0;
    const libre = snapshot.total ? (snapshot.libres / snapshot.total) * 100 : 0;
    const ocupadoEnd = ocupado;
    const libreEnd = ocupado + libre;
    return `conic-gradient(#2563eb 0 ${ocupadoEnd}%, #059669 ${ocupadoEnd}% ${libreEnd}%, #d97706 ${libreEnd}% 100%)`;
  }

  private buildOccupancy(puestos: Puesto[]): OccupancySnapshot {
    const ocupados = puestos.filter((puesto) => puesto.estadoPuesto === 'OCUPADO').length;
    const libres = puestos.filter((puesto) => puesto.estadoPuesto === 'LIBRE').length;
    const mantenimiento = puestos.filter(
      (puesto) => puesto.estadoPuesto === 'MANTENIMIENTO',
    ).length;
    const total = puestos.length;
    return {
      total,
      ocupados,
      libres,
      mantenimiento,
      porcentajeOcupacion: total ? (ocupados / total) * 100 : 0,
    };
  }

  private buildPaymentMethods(pagos: FlujoCajaReporte[]): BarItem[] {
    const totals = new Map<MetodoPago, number>();
    pagos.forEach((pago) => {
      totals.set(pago.metodoPago, (totals.get(pago.metodoPago) ?? 0) + pago.montoPagado);
    });
    const max = Math.max(...Array.from(totals.values()), 0);
    return (['EFECTIVO', 'TRANSFERENCIA', 'YAPE_PLIN', 'TARJETA'] as MetodoPago[])
      .map((method) => {
        const value = totals.get(method) ?? 0;
        return {
          label: method.replace('_', '/'),
          value,
          percent: max ? (value / max) * 100 : 0,
          formatted: this.money(value),
        };
      })
      .filter((item) => item.value > 0);
  }

  private buildQuotaStatus(cuotas: Cuota[]): BarItem[] {
    const labels = ['PENDIENTE', 'PAGADO', 'ANULADO', 'EXONERADO'];
    const max = Math.max(
      ...labels.map((label) => cuotas.filter((cuota) => cuota.estadoCuota === label).length),
      0,
    );
    return labels.map((label) => {
      const value = cuotas.filter((cuota) => cuota.estadoCuota === label).length;
      return {
        label,
        value,
        percent: max ? (value / max) * 100 : 0,
        formatted: String(value),
      };
    });
  }

  private buildTopDebts(deudas: DeudaPendienteReporte[]): DebtRow[] {
    return [...deudas]
      .sort((a, b) => b.montoTotal - a.montoTotal)
      .slice(0, 5)
      .map((deuda) => ({
        idPuesto: deuda.idPuesto,
        idCuota: deuda.idCuota,
        periodo: `${deuda.periodoMes}/${deuda.periodoAnio}`,
        monto: deuda.montoTotal,
      }));
  }

  private buildRecentActivity(
    transferencias: Transferencia[],
    eventos: AuditoriaEvento[],
    anulaciones: AuditoriaAnulacion[],
  ): ActivityRow[] {
    const rows: ActivityRow[] = [
      ...transferencias.map((transferencia) => ({
        label: `Transferencia #${transferencia.idTransferencia}`,
        detail: `Puesto ${transferencia.idPuesto} - socio entrante ${transferencia.idSocioEntrante}`,
        date: transferencia.fechaTramite,
        type: 'transferencia' as const,
      })),
      ...eventos.map((evento) => ({
        label: evento.tipoEvento,
        detail: `${evento.modulo} - ${evento.entidadAfectada} #${evento.idRegistroAfectado}`,
        date: evento.fechaEvento,
        type: 'evento' as const,
      })),
      ...anulaciones.map((anulacion) => ({
        label: `Anulacion ${anulacion.tipoAnulacion}`,
        detail: `Registro #${anulacion.idRegistroAfectado}`,
        date: anulacion.fechaAnulacion,
        type: 'anulacion' as const,
      })),
    ];

    return rows.sort((a, b) => this.dateValue(b.date) - this.dateValue(a.date)).slice(0, 6);
  }

  private isOverdue(cuota: Cuota): boolean {
    return Boolean(cuota.fechaVencimiento && cuota.fechaVencimiento < this.todayIso());
  }

  private todayIso(): string {
    const now = new Date();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${now.getFullYear()}-${month}-${day}`;
  }

  private dateValue(date: string): number {
    return new Date(date).getTime() || 0;
  }
}
