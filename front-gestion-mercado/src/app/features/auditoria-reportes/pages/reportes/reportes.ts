import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { TableColumn } from '../../../../core/models/table.models';
import { AuditoriaReportesApi } from '../../../../core/services/auditoria-reportes-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-reportes',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './reportes.html',
  styleUrl: './reportes.css',
})
export class Reportes {
  tipo: 'padron-habiles' | 'morosidad' | 'flujo-caja-diario' = 'padron-habiles';
  fecha = '';
  idTurno: number | null = null;
  rows = signal<unknown[]>([]);
  columns = signal<TableColumn[]>([]);
  loading = signal(false);
  error = signal('');

  constructor(private readonly auditoriaApi: AuditoriaReportesApi) {
    this.cargar();
  }

  cargar(): void {
    this.loading.set(true);
    this.error.set('');
    this.rows.set([]);
    this.columns.set(this.columnsFor(this.tipo));

    let operation: Observable<unknown[]>;

    if (this.tipo === 'padron-habiles') {
      operation = this.auditoriaApi.padronHabiles();
    } else if (this.tipo === 'morosidad') {
      operation = this.auditoriaApi.morosidad();
    } else {
      operation = this.auditoriaApi.flujoCajaDiario(this.fecha || undefined, this.idTurno);
    }

    operation.subscribe({
      next: (rows) => {
        this.rows.set(rows);
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

  abrirPdf(): void {
    this.auditoriaApi
      .reportePdf(
        this.tipo,
        this.tipo === 'flujo-caja-diario' ? this.fecha || undefined : undefined,
        this.tipo === 'flujo-caja-diario' ? this.idTurno : undefined,
      )
      .subscribe({
        next: (blob) => {
          const url = URL.createObjectURL(blob);
          window.open(url, '_blank', 'noopener');
          setTimeout(() => URL.revokeObjectURL(url), 1000);
        },
        error: (error: unknown) => {
          this.error.set(httpErrorMessage(error));
        },
      });
  }

  private columnsFor(tipo: 'padron-habiles' | 'morosidad' | 'flujo-caja-diario'): TableColumn[] {
    if (tipo === 'padron-habiles') {
      return [
        { key: 'idSocio', label: 'Socio' },
        { key: 'dni', label: 'DNI' },
        { key: 'nombres', label: 'Nombres' },
        { key: 'apellidos', label: 'Apellidos' },
        { key: 'correo', label: 'Correo' },
        { key: 'telefono', label: 'Telefono' },
      ];
    }

    if (tipo === 'morosidad') {
      return [
        { key: 'idCuota', label: 'Cuota' },
        { key: 'idPuesto', label: 'Puesto' },
        { key: 'idContrato', label: 'Contrato' },
        { key: 'idConcepto', label: 'Concepto' },
        { key: 'periodoMes', label: 'Mes' },
        { key: 'periodoAnio', label: 'Anio' },
        { key: 'montoTotal', label: 'Monto', type: 'currency' },
        { key: 'fechaVencimiento', label: 'Vencimiento', type: 'date' },
      ];
    }

    return [
      { key: 'idPago', label: 'Pago' },
      { key: 'idTurno', label: 'Turno' },
      { key: 'idCuota', label: 'Cuota' },
      { key: 'idUsuarioCobro', label: 'Usuario' },
      { key: 'metodoPago', label: 'Metodo', type: 'status' },
      { key: 'montoPagado', label: 'Monto', type: 'currency' },
      { key: 'fechaPago', label: 'Fecha', type: 'date' },
    ];
  }
}
