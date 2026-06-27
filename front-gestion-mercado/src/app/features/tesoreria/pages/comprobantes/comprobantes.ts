import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { DetailPanel } from '../../../../shared/components/detail-panel/detail-panel';
import { StatusBadge } from '../../../../shared/components/status-badge/status-badge';
import { Comprobante } from '../../../../core/models/tesoreria.models';
import { TableColumn } from '../../../../core/models/table.models';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-comprobantes',
  imports: [FormsModule, PageHeader, DataTable, DetailPanel, StatusBadge],
  templateUrl: './comprobantes.html',
  styleUrl: './comprobantes.css',
})
export class Comprobantes {
  readonly columns: TableColumn[] = [
    { key: 'idComprobante', label: 'Comprobante' },
    { key: 'idPago', label: 'Pago' },
    { key: 'idCuota', label: 'Cuota' },
    { key: 'numeroComprobante', label: 'Numero' },
    { key: 'fechaEmision', label: 'Emision', type: 'date' },
    { key: 'montoTotal', label: 'Monto', type: 'currency' },
    { key: 'metodoPago', label: 'Metodo', type: 'status' },
    { key: 'estadoComprobante', label: 'Estado', type: 'status' },
  ];

  criterio: 'comprobante' | 'pago' | 'cuota' | 'numero' = 'comprobante';
  valor = '';
  comprobantes = signal<Comprobante[]>([]);
  comprobante = signal<Comprobante | null>(null);
  loading = signal(false);
  error = signal('');

  constructor(private readonly tesoreriaApi: TesoreriaApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.tesoreriaApi.listarComprobantes().subscribe({
      next: (comprobantes) => {
        this.comprobantes.set(comprobantes);
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

  buscar(): void {
    this.error.set('');
    this.comprobante.set(null);
    const value = this.valor.trim();

    if (!value) {
      this.error.set('Indica el valor a consultar.');
      return;
    }

    const operation =
      this.criterio === 'comprobante'
        ? this.tesoreriaApi.obtenerComprobante(Number(value))
        : this.criterio === 'pago'
          ? this.tesoreriaApi.obtenerComprobantePorPago(Number(value))
          : this.criterio === 'cuota'
            ? this.tesoreriaApi.obtenerComprobantePorCuota(Number(value))
            : this.tesoreriaApi.obtenerComprobantePorNumero(value);

    operation.subscribe({
      next: (comprobante) => {
        this.comprobante.set(comprobante);
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }
}
