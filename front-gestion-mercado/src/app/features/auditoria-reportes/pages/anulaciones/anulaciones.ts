import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import {
  AuditoriaAnulacion,
  AuditoriaAnulacionRequest,
} from '../../../../core/models/auditoria.models';
import { TableColumn } from '../../../../core/models/table.models';
import { AuditoriaReportesApi } from '../../../../core/services/auditoria-reportes-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-anulaciones',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './anulaciones.html',
  styleUrl: './anulaciones.css',
})
export class Anulaciones {
  readonly columns: TableColumn[] = [
    { key: 'idAuditoria', label: 'ID' },
    { key: 'tipoAnulacion', label: 'Tipo', type: 'status' },
    { key: 'idRegistroAfectado', label: 'Registro' },
    { key: 'idUsuario', label: 'Usuario' },
    { key: 'motivoSustento', label: 'Motivo' },
    { key: 'fechaAnulacion', label: 'Fecha', type: 'date' },
  ];

  anulaciones = signal<AuditoriaAnulacion[]>([]);
  form: AuditoriaAnulacionRequest = {
    tipoAnulacion: 'CUOTA',
    idRegistroAfectado: null,
    idUsuario: null,
    motivoSustento: '',
  };
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly auditoriaApi: AuditoriaReportesApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    this.auditoriaApi.listarAnulaciones().subscribe({
      next: (anulaciones) => {
        this.anulaciones.set(anulaciones);
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

    if (this.form.idRegistroAfectado === null || this.form.idUsuario === null) {
      this.error.set('Completa registro afectado y usuario.');
      return;
    }

    this.auditoriaApi.registrarAnulacion(this.form).subscribe({
      next: () => {
        this.message.set('Anulacion auditada.');
        setTimeout(() => this.message.set(''), 3000);
        this.form = {
          tipoAnulacion: 'CUOTA',
          idRegistroAfectado: null,
          idUsuario: null,
          motivoSustento: '',
        };
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }
}
