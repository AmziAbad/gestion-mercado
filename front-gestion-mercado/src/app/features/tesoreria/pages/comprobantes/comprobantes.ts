import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DetailPanel } from '../../../../shared/components/detail-panel/detail-panel';
import { StatusBadge } from '../../../../shared/components/status-badge/status-badge';
import { Comprobante } from '../../../../core/models/tesoreria.models';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-comprobantes',
  imports: [FormsModule, PageHeader, DetailPanel, StatusBadge],
  templateUrl: './comprobantes.html',
  styleUrl: './comprobantes.css',
})
export class Comprobantes {
  criterio: 'comprobante' | 'pago' = 'comprobante';
  valor: number | null = null;
  comprobante = signal<Comprobante | null>(null);
  error = signal('');

  constructor(private readonly tesoreriaApi: TesoreriaApi) {}

  buscar(): void {
    this.error.set('');
    this.comprobante.set(null);

    if (this.valor === null) {
      this.error.set('Indica el ID a consultar.');
      return;
    }

    const operation =
      this.criterio === 'comprobante'
        ? this.tesoreriaApi.obtenerComprobante(this.valor)
        : this.tesoreriaApi.obtenerComprobantePorPago(this.valor);

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
