import { Component, signal } from '@angular/core';
import { catchError, forkJoin, of } from 'rxjs';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { SummaryCard } from '../../../../shared/components/summary-card/summary-card';
import { PatrimonioApi } from '../../../../core/services/patrimonio-api';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { AuditoriaReportesApi } from '../../../../core/services/auditoria-reportes-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';
import { Session } from '../../../../core/services/session';
import { ROLE_GROUPS } from '../../../../core/constants/roles';

@Component({
  selector: 'app-dashboard',
  imports: [PageHeader, SummaryCard],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  loading = signal(false);
  error = signal('');

  summary = signal({
    socios: 0,
    puestos: 0,
    contratosActivos: 0,
    cuotasPendientes: 0,
    transferencias: 0,
    eventosAuditoria: 0,
  });

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

    const puedeConsultarTesoreria = this.session.hasAnyRole(ROLE_GROUPS.tesoreria);
    const puedeConsultarAuditoria = this.session.hasAnyRole(ROLE_GROUPS.auditoriaReportes);

    forkJoin({
      socios: this.patrimonioApi.listarSocios().pipe(catchError(() => of([]))),
      puestos: this.patrimonioApi.listarPuestos().pipe(catchError(() => of([]))),
      contratos: this.patrimonioApi.listarContratosActivos().pipe(catchError(() => of([]))),
      cuotas: puedeConsultarTesoreria
        ? this.tesoreriaApi.listarCuotas().pipe(catchError(() => of([])))
        : of([]),
      transferencias: this.patrimonioApi.listarTransferencias().pipe(catchError(() => of([]))),
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
