import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DetailPanel } from '../../../../shared/components/detail-panel/detail-panel';
import { StatusBadge } from '../../../../shared/components/status-badge/status-badge';
import { Pago, PagoRequest } from '../../../../core/models/tesoreria.models';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-pagos',
  imports: [FormsModule, PageHeader, DetailPanel, StatusBadge],
  templateUrl: './pagos.html',
  styleUrl: './pagos.css',
})
export class Pagos {
  pagoRequest: PagoRequest = {
    idCuota: null,
    metodoPago: 'EFECTIVO',
    numeroOperacion: null,
  };
  idPagoBusqueda: number | null = null;
  motivoExtorno = '';
  pago = signal<Pago | null>(null);
  message = signal('');
  error = signal('');

  constructor(private readonly tesoreriaApi: TesoreriaApi) {}

  registrar(): void {
    this.message.set('');
    this.error.set('');

    if (this.pagoRequest.idCuota === null) {
      this.error.set('Indica la cuota a pagar.');
      return;
    }

    this.tesoreriaApi.registrarPago(this.pagoRequest).subscribe({
      next: (pago) => {
        this.pago.set(pago);
        this.message.set('Pago registrado.');
        setTimeout(() => this.message.set(''), 3000);
        this.pagoRequest = { idCuota: null, metodoPago: 'EFECTIVO', numeroOperacion: null };
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  buscar(): void {
    this.message.set('');
    this.error.set('');

    if (this.idPagoBusqueda === null) {
      this.error.set('Indica el ID del pago.');
      return;
    }

    this.tesoreriaApi.obtenerPago(this.idPagoBusqueda).subscribe({
      next: (pago) => {
        this.pago.set(pago);
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  extornar(): void {
    this.message.set('');
    this.error.set('');

    if (!this.pago() || !this.motivoExtorno.trim()) {
      this.error.set('Busca un pago y escribe el motivo de extorno.');
      return;
    }

    this.tesoreriaApi
      .extornarPago(this.pago()!.idPago, { motivoExtorno: this.motivoExtorno })
      .subscribe({
        next: (pago) => {
          this.pago.set(pago);
          this.motivoExtorno = '';
          this.message.set('Pago extornado.');
          setTimeout(() => this.message.set(''), 3000);
        },
        error: (error: unknown) => {
          this.error.set(httpErrorMessage(error));
        },
      });
  }
}
