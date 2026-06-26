import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { TableColumn } from '../../../../core/models/table.models';
import { Transferencia, TransferenciaRequest } from '../../../../core/models/patrimonio.models';
import { PatrimonioApi } from '../../../../core/services/patrimonio-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-transferencias',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './transferencias.html',
  styleUrl: './transferencias.css',
})
export class Transferencias {
  readonly columns: TableColumn[] = [
    { key: 'idTransferencia', label: 'ID' },
    { key: 'idPuesto', label: 'Puesto' },
    { key: 'idContratoSaliente', label: 'Contrato saliente' },
    { key: 'idSocioSaliente', label: 'Socio saliente' },
    { key: 'idSocioEntrante', label: 'Socio entrante' },
    { key: 'idContratoEntrante', label: 'Contrato entrante' },
    { key: 'idUsuarioTramite', label: 'Usuario tramite' },
    { key: 'costoTransferencia', label: 'Costo', type: 'currency' },
    { key: 'deudaValidada', label: 'Deuda validada', type: 'boolean' },
    { key: 'asumeDeuda', label: 'Asume deuda', type: 'boolean' },
    { key: 'montoDeudaAsumida', label: 'Deuda asumida', type: 'currency' },
    { key: 'estadoTransferencia', label: 'Estado', type: 'status' },
    { key: 'observacion', label: 'Observacion' },
    { key: 'fechaTramite', label: 'Fecha', type: 'date' },
  ];

  transferencias = signal<Transferencia[]>([]);
  form: TransferenciaRequest = {
    idPuesto: null,
    idSocioEntrante: null,
    costoTransferencia: 0,
    asumeDeuda: false,
    observacion: null,
    fechaInicio: null,
  };
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly patrimonioApi: PatrimonioApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    this.patrimonioApi.listarTransferencias().subscribe({
      next: (transferencias) => {
        this.transferencias.set(transferencias);
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

  registrar(): void {
    this.message.set('');
    this.error.set('');

    if (this.form.idPuesto === null || this.form.idSocioEntrante === null) {
      this.error.set('Indica puesto y socio entrante.');
      return;
    }

    this.patrimonioApi.registrarTransferencia(this.form).subscribe({
      next: () => {
        this.message.set('Transferencia registrada.');
        this.form = {
          idPuesto: null,
          idSocioEntrante: null,
          costoTransferencia: 0,
          asumeDeuda: false,
          observacion: null,
          fechaInicio: null,
        };
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }
}
