import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { Modal } from '../../../../shared/components/modal/modal';
import { TableColumn } from '../../../../core/models/table.models';
import {
  Turno,
  TurnoAperturaRequest,
  TurnoCierreRequest,
} from '../../../../core/models/tesoreria.models';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-caja',
  imports: [FormsModule, PageHeader, DataTable, Modal],
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
    montoRecaudado: null as number | null,
    observacionCierre: null as string | null,
  };
  showAperturaForm = false;
  showCierreForm = false;
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
        this.closeAperturaForm();
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

    if (this.cierre.montoRecaudado === null) {
      this.error.set('Indica el monto contado en caja.');
      return;
    }

    const request: TurnoCierreRequest = {
      montoRecaudado: this.cierre.montoRecaudado,
      observacionCierre: this.cierre.observacionCierre,
    };

    this.tesoreriaApi.cerrarTurno(this.cierre.idTurno, request).subscribe({
      next: () => {
        this.message.set('Turno cerrado.');
        setTimeout(() => this.message.set(''), 3000);
        this.closeCierreForm();
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  openAperturaForm(): void {
    this.apertura = { montoInicial: 0, observacionApertura: null };
    this.showAperturaForm = true;
  }

  closeAperturaForm(): void {
    this.showAperturaForm = false;
    this.apertura = { montoInicial: 0, observacionApertura: null };
  }

  openCierreForm(): void {
    this.cierre = { idTurno: null, montoRecaudado: null, observacionCierre: null };
    this.showCierreForm = true;
  }

  closeCierreForm(): void {
    this.showCierreForm = false;
    this.cierre = { idTurno: null, montoRecaudado: null, observacionCierre: null };
  }
}
