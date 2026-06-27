import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { TableColumn } from '../../../../core/models/table.models';
import { Turno, TurnoAperturaRequest } from '../../../../core/models/tesoreria.models';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-caja',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './caja.html',
  styleUrl: './caja.css',
})
export class Caja {
  readonly columns: TableColumn[] = [
    { key: 'idTurno', label: 'Turno' },
    { key: 'idUsuario', label: 'Usuario' },
    { key: 'fechaApertura', label: 'Apertura', type: 'date' },
    { key: 'fechaCierre', label: 'Cierre', type: 'date' },
    { key: 'montoInicial', label: 'Inicial', type: 'currency' },
    { key: 'montoRecaudado', label: 'Recaudado', type: 'currency' },
    { key: 'montoEsperado', label: 'Esperado', type: 'currency' },
    { key: 'diferencia', label: 'Diferencia', type: 'currency' },
    { key: 'estadoTurno', label: 'Estado', type: 'status' },
    { key: 'observacionApertura', label: 'Obs. apertura' },
    { key: 'observacionCierre', label: 'Obs. cierre' },
  ];

  turnos = signal<Turno[]>([]);
  fecha = '';
  apertura: TurnoAperturaRequest = {
    montoInicial: 0,
    observacionApertura: null,
  };
  cierre = {
    idTurno: null as number | null,
    observacionCierre: null as string | null,
  };
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly tesoreriaApi: TesoreriaApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.tesoreriaApi.listarTurnos(this.fecha || undefined).subscribe({
      next: (turnos) => {
        this.turnos.set(turnos);
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

  aperturar(): void {
    this.message.set('');
    this.error.set('');
    this.tesoreriaApi.aperturarTurno(this.apertura).subscribe({
      next: () => {
        this.message.set('Turno aperturado.');
        setTimeout(() => this.message.set(''), 3000);
        this.apertura = { montoInicial: 0, observacionApertura: null };
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  cerrar(): void {
    this.message.set('');
    this.error.set('');

    if (this.cierre.idTurno === null) {
      this.error.set('Indica el turno a cerrar.');
      return;
    }

    this.tesoreriaApi
      .cerrarTurno(this.cierre.idTurno, { observacionCierre: this.cierre.observacionCierre })
      .subscribe({
        next: () => {
          this.message.set('Turno cerrado.');
          setTimeout(() => this.message.set(''), 3000);
          this.cierre = { idTurno: null, observacionCierre: null };
          this.load();
        },
        error: (error: unknown) => {
          this.error.set(httpErrorMessage(error));
        },
      });
  }
}
