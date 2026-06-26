import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import {
  Cuota,
  CuotaEspecificaRequest,
  CuotaMasivaRequest,
} from '../../../../core/models/tesoreria.models';
import { TableActionEvent, TableColumn } from '../../../../core/models/table.models';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-cuotas',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './cuotas.html',
  styleUrl: './cuotas.css',
})
export class Cuotas {
  readonly columns: TableColumn[] = [
    { key: 'idCuota', label: 'ID' },
    { key: 'idPuesto', label: 'Puesto' },
    { key: 'idContrato', label: 'Contrato' },
    { key: 'idConcepto', label: 'Concepto' },
    { key: 'periodoMes', label: 'Mes' },
    { key: 'periodoAnio', label: 'Anio' },
    { key: 'montoTotal', label: 'Monto', type: 'currency' },
    { key: 'estadoCuota', label: 'Estado', type: 'status' },
  ];

  readonly actions = [
    { id: 'anular', label: 'Anular', tone: 'danger' as const },
    { id: 'exonerar', label: 'Exonerar', tone: 'secondary' as const },
  ];

  cuotas = signal<Cuota[]>([]);
  masiva: CuotaMasivaRequest = this.emptyMasiva();
  especifica: CuotaEspecificaRequest = this.emptyEspecifica();
  motivoOperacion = '';
  generarReemplazo = false;
  montoReemplazo: number | null = null;
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly tesoreriaApi: TesoreriaApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.tesoreriaApi.listarCuotas().subscribe({
      next: (cuotas) => {
        this.cuotas.set(cuotas);
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

  generarMasivas(): void {
    this.error.set('');
    this.message.set('');

    if (
      this.masiva.idConcepto === null ||
      this.masiva.periodoMes === null ||
      this.masiva.periodoAnio === null
    ) {
      this.error.set('Completa concepto, mes y anio.');
      return;
    }

    this.tesoreriaApi.generarCuotasMasivas(this.masiva).subscribe({
      next: (cuotas) => {
        this.message.set(`${cuotas.length} cuotas generadas.`);
        this.masiva = this.emptyMasiva();
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  generarEspecifica(): void {
    this.error.set('');
    this.message.set('');

    if (
      this.especifica.idPuesto === null ||
      this.especifica.idConcepto === null ||
      this.especifica.montoTotal === null
    ) {
      this.error.set('Completa puesto, concepto y monto.');
      return;
    }

    this.tesoreriaApi.generarCuotaEspecifica(this.especifica).subscribe({
      next: () => {
        this.message.set('Cuota especifica generada.');
        this.especifica = this.emptyEspecifica();
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  handleAction(event: TableActionEvent): void {
    const cuota = event.row as Cuota;

    if (!this.motivoOperacion.trim()) {
      this.error.set('Escribe un motivo antes de anular o exonerar.');
      return;
    }

    const operation =
      event.action.id === 'anular'
        ? this.tesoreriaApi.anularCuota(cuota.idCuota, {
            motivoAnulacion: this.motivoOperacion,
            generarReemplazo: this.generarReemplazo,
            montoReemplazo: this.montoReemplazo,
          })
        : this.tesoreriaApi.exonerarCuota(cuota.idCuota, {
            motivoExoneracion: this.motivoOperacion,
          });

    operation.subscribe({
      next: () => {
        this.message.set(event.action.id === 'anular' ? 'Cuota anulada.' : 'Cuota exonerada.');
        this.motivoOperacion = '';
        this.generarReemplazo = false;
        this.montoReemplazo = null;
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  private emptyMasiva(): CuotaMasivaRequest {
    return {
      idConcepto: null,
      periodoMes: new Date().getMonth() + 1,
      periodoAnio: new Date().getFullYear(),
      fechaVencimiento: null,
    };
  }

  private emptyEspecifica(): CuotaEspecificaRequest {
    return {
      idPuesto: null,
      idContrato: null,
      idConcepto: null,
      montoTotal: null,
      periodoMes: new Date().getMonth() + 1,
      periodoAnio: new Date().getFullYear(),
      fechaVencimiento: null,
    };
  }
}
