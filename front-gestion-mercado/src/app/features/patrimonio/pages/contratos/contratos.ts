import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { Contrato, ContratoRequest } from '../../../../core/models/patrimonio.models';
import { TableActionEvent, TableColumn } from '../../../../core/models/table.models';
import { PatrimonioApi } from '../../../../core/services/patrimonio-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-contratos',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './contratos.html',
  styleUrl: './contratos.css',
})
export class Contratos {
  readonly columns: TableColumn[] = [
    { key: 'idContrato', label: 'ID' },
    { key: 'idPuesto', label: 'ID puesto' },
    { key: 'codigoPuesto', label: 'Puesto' },
    { key: 'idSocio', label: 'ID socio' },
    { key: 'nombreSocio', label: 'Socio' },
    { key: 'fechaInicio', label: 'Inicio', type: 'date' },
    { key: 'fechaFin', label: 'Fin', type: 'date' },
    { key: 'estadoContrato', label: 'Estado', type: 'status' },
    { key: 'motivoCierre', label: 'Motivo cierre' },
    { key: 'idUsuarioRegistro', label: 'Usuario registro' },
    { key: 'fechaRegistro', label: 'Fecha registro', type: 'date' },
  ];

  readonly actions = [{ id: 'finalizar', label: 'Finalizar', tone: 'danger' as const }];

  contratos = signal<Contrato[]>([]);
  form: ContratoRequest = { idPuesto: null, idSocio: null, fechaInicio: null };
  motivoCierre = '';
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly patrimonioApi: PatrimonioApi) {
    this.load();
  }

  finalizarModalOpen = signal(false);
  contratoAFinalizar = signal<Contrato | null>(null);

  load(): void {
    this.loading.set(true);
    this.error.set('');

    this.patrimonioApi.listarContratosActivos().subscribe({
      next: (contratos) => {
        this.contratos.set(contratos);
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

    if (this.form.idPuesto === null || this.form.idSocio === null) {
      this.error.set('Indica puesto y socio para aperturar el contrato.');
      return;
    }

    this.patrimonioApi.aperturarContrato(this.form).subscribe({
      next: () => {
        this.message.set('Contrato aperturado.');
        this.form = { idPuesto: null, idSocio: null, fechaInicio: null };
        this.load();
        setTimeout(() => this.message.set(''), 3000);
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  handleAction(event: TableActionEvent): void {
    const contrato = event.row as Contrato;
    this.contratoAFinalizar.set(contrato);
    this.finalizarModalOpen.set(true);
    this.motivoCierre = '';
  }

  confirmarFinalizacion(): void {
    const contrato = this.contratoAFinalizar();
    if (!contrato) return;

    if (!this.motivoCierre.trim()) {
      this.error.set('Escribe un motivo de cierre antes de finalizar.');
      return;
    }

    this.patrimonioApi
      .finalizarContrato(contrato.idContrato, { motivoCierre: this.motivoCierre })
      .subscribe({
        next: () => {
          this.message.set('Contrato finalizado.');
          this.closeFinalizarModal();
          this.load();
          setTimeout(() => this.message.set(''), 3000);
        },
        error: (error: unknown) => {
          this.error.set(httpErrorMessage(error));
        },
      });
  }

  closeFinalizarModal(): void {
    this.finalizarModalOpen.set(false);
    this.contratoAFinalizar.set(null);
    this.motivoCierre = '';
  }
}
