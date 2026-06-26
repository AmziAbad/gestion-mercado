import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { AuditoriaEvento, AuditoriaEventoRequest } from '../../../../core/models/auditoria.models';
import { TableColumn } from '../../../../core/models/table.models';
import { AuditoriaReportesApi } from '../../../../core/services/auditoria-reportes-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-eventos',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './eventos.html',
  styleUrl: './eventos.css',
})
export class Eventos {
  readonly columns: TableColumn[] = [
    { key: 'idEvento', label: 'ID' },
    { key: 'modulo', label: 'Modulo' },
    { key: 'tipoEvento', label: 'Evento' },
    { key: 'entidadAfectada', label: 'Entidad' },
    { key: 'idRegistroAfectado', label: 'Registro' },
    { key: 'idUsuario', label: 'Usuario' },
    { key: 'fechaEvento', label: 'Fecha', type: 'date' },
  ];

  eventos = signal<AuditoriaEvento[]>([]);
  form: AuditoriaEventoRequest = {
    modulo: '',
    tipoEvento: '',
    entidadAfectada: '',
    idRegistroAfectado: null,
    idUsuario: null,
    descripcion: '',
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
    this.auditoriaApi.listarEventos().subscribe({
      next: (eventos) => {
        this.eventos.set(eventos);
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

    this.auditoriaApi.registrarEvento(this.form).subscribe({
      next: () => {
        this.message.set('Evento registrado.');
        setTimeout(() => this.message.set(''), 3000);
        this.form = {
          modulo: '',
          tipoEvento: '',
          entidadAfectada: '',
          idRegistroAfectado: null,
          idUsuario: null,
          descripcion: '',
        };
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }
}
