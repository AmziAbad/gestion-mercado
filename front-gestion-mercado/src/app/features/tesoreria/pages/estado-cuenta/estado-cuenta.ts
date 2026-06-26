import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { SummaryCard } from '../../../../shared/components/summary-card/summary-card';
import { EstadoCuenta as EstadoCuentaModel } from '../../../../core/models/tesoreria.models';
import { TableColumn } from '../../../../core/models/table.models';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-estado-cuenta',
  imports: [FormsModule, PageHeader, DataTable, SummaryCard],
  templateUrl: './estado-cuenta.html',
  styleUrl: './estado-cuenta.css',
})
export class EstadoCuenta {
  readonly cuotaColumns: TableColumn[] = [
    { key: 'idCuota', label: 'Cuota' },
    { key: 'idPuesto', label: 'Puesto' },
    { key: 'idConcepto', label: 'Concepto' },
    { key: 'periodoMes', label: 'Mes' },
    { key: 'periodoAnio', label: 'Anio' },
    { key: 'montoTotal', label: 'Monto', type: 'currency' },
    { key: 'estadoCuota', label: 'Estado', type: 'status' },
  ];

  criterio: 'puesto' | 'socio' | 'dni' = 'puesto';
  valor = '';
  estadoCuenta = signal<EstadoCuentaModel | null>(null);
  loading = signal(false);
  error = signal('');

  constructor(private readonly tesoreriaApi: TesoreriaApi) {}

  consultar(): void {
    this.error.set('');
    this.loading.set(true);
    this.estadoCuenta.set(null);

    const value = this.valor.trim();

    if (!value) {
      this.error.set('Indica un valor de busqueda.');
      this.loading.set(false);
      return;
    }

    const operation =
      this.criterio === 'puesto'
        ? this.tesoreriaApi.estadoCuentaPorPuesto(Number(value))
        : this.criterio === 'socio'
          ? this.tesoreriaApi.estadoCuentaPorSocio(Number(value))
          : this.tesoreriaApi.estadoCuentaPorDni(value);

    operation.subscribe({
      next: (estadoCuenta) => {
        this.estadoCuenta.set(estadoCuenta);
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
