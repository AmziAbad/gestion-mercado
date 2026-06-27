import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { Modal } from '../../../../shared/components/modal/modal';
import { AuditoriaEvento, AuditoriaEventoRequest } from '../../../../core/models/auditoria.models';
import { TableColumn } from '../../../../core/models/table.models';
import { AuditoriaReportesApi } from '../../../../core/services/auditoria-reportes-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-eventos',
  imports: [FormsModule, PageHeader, DataTable, Modal],
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
    { key: 'descripcion', label: 'Descripcion' },
    { key: 'fechaEvento', label: 'Fecha', type: 'date' },
  ];

  eventos = signal<AuditoriaEvento[]>([]);
  filtro = {
    entidadAfectada: '',
    idRegistroAfectado: null as number | null,
  };
  form: AuditoriaEventoRequest = {
    modulo: '',
    tipoEvento: '',
    entidadAfectada: '',
    idRegistroAfectado: null,
    idUsuario: null,
    descripcion: '',
  };
  showForm = false;
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly auditoriaApi: AuditoriaReportesApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.filtro = { entidadAfectada: '', idRegistroAfectado: null };
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

  filtrarPorRegistro(): void {
    this.loading.set(true);
    this.error.set('');

    if (!this.filtro.entidadAfectada.trim() || this.filtro.idRegistroAfectado === null) {
      this.error.set('Indica entidad afectada e ID de registro.');
      this.loading.set(false);
      return;
    }

    this.auditoriaApi
      .listarEventosPorRegistro(this.filtro.entidadAfectada.trim(), this.filtro.idRegistroAfectado)
      .subscribe({
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
        this.closeForm();
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  openCreateForm(): void {
    this.form = this.emptyForm();
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
    this.form = this.emptyForm();
  }

  private emptyForm(): AuditoriaEventoRequest {
    return {
      modulo: '',
      tipoEvento: '',
      entidadAfectada: '',
      idRegistroAfectado: null,
      idUsuario: null,
      descripcion: '',
    };
  }
}
