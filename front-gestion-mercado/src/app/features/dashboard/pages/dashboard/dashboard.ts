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

interface DashboardSummary {
  socios: number;
  puestos: number;
  contratosActivos: number;
  cuotasPendientes: number;
  transferencias: number;
  eventosAuditoria: number;
}

interface DashboardMetric {
  key: keyof DashboardSummary;
  label: string;
  helper: string;
  icon: string;
  roles: readonly RolUsuario[];
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
    contratosActivos: 0,
    cuotasPendientes: 0,
    transferencias: 0,
    eventosAuditoria: 0,
  });

  readonly metricDefinitions: DashboardMetric[] = [
    {
      key: 'socios',
      label: 'Socios registrados',
      helper: 'Padron administrativo',
      icon: 'socios',
      roles: ROLE_GROUPS.patrimonio,
    },
    {
      key: 'puestos',
      label: 'Puestos registrados',
      helper: 'Unidades fisicas',
      icon: 'puestos',
      roles: ROLE_GROUPS.patrimonio,
    },
    {
      key: 'contratosActivos',
      label: 'Contratos activos',
      helper: 'Ocupacion vigente',
      icon: 'contratos',
      roles: ROLE_GROUPS.patrimonio,
    },
    {
      key: 'cuotasPendientes',
      label: 'Cuotas pendientes',
      helper: 'Deuda por cobrar',
      icon: 'cuotas',
      roles: ROLE_GROUPS.tesoreria,
    },
    {
      key: 'transferencias',
      label: 'Transferencias',
      helper: 'Traspasos registrados',
      icon: 'transferencias',
      roles: ROLE_GROUPS.patrimonio,
    },
    {
      key: 'eventosAuditoria',
      label: 'Eventos de auditoria',
      helper: 'Trazabilidad',
      icon: 'eventos',
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

    const puedeConsultarPatrimonio = this.session.hasAnyRole(ROLE_GROUPS.patrimonio);
    const puedeConsultarTesoreria = this.session.hasAnyRole(ROLE_GROUPS.tesoreria);
    const puedeConsultarAuditoria = this.session.hasAnyRole(ROLE_GROUPS.auditoriaReportes);

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
      cuotas: puedeConsultarTesoreria
        ? this.tesoreriaApi.listarCuotas().pipe(catchError(() => of([])))
        : of([]),
      transferencias: puedeConsultarPatrimonio
        ? this.patrimonioApi.listarTransferencias().pipe(catchError(() => of([])))
        : of([]),
      eventos: puedeConsultarAuditoria
        ? this.auditoriaApi.listarEventos().pipe(catchError(() => of([])))
        : of([]),
    }).subscribe({
      next: ({ socios, puestos, contratos, cuotas, transferencias, eventos }) => {
        this.summary.set({
          socios: socios.length,
          puestos: puestos.length,
          contratosActivos: contratos.length,
          cuotasPendientes: cuotas.filter((cuota) => cuota.estadoCuota === 'PENDIENTE').length,
          transferencias: transferencias.length,
          eventosAuditoria: eventos.length,
        });
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
}
