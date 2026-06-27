import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { Concepto, ConceptoRequest } from '../../../../core/models/tesoreria.models';
import { TableAction, TableActionEvent, TableColumn } from '../../../../core/models/table.models';
import { TesoreriaApi } from '../../../../core/services/tesoreria-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-conceptos',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './conceptos.html',
  styleUrl: './conceptos.css',
})
export class Conceptos {
  readonly columns: TableColumn[] = [
    { key: 'idConcepto', label: 'ID' },
    { key: 'nombreConcepto', label: 'Concepto' },
    { key: 'descripcion', label: 'Descripcion' },
    { key: 'tipoCobro', label: 'Tipo', type: 'status' },
    { key: 'periodicidad', label: 'Periodicidad', type: 'status' },
    { key: 'montoFijo', label: 'Monto fijo', type: 'currency' },
    { key: 'costoTotalProrrateo', label: 'Prorrateo', type: 'currency' },
    { key: 'activo', label: 'Activo', type: 'boolean' },
    { key: 'fechaRegistro', label: 'Fecha registro', type: 'date' },
  ];

  readonly actions: TableAction[] = [
    { id: 'edit', label: 'Editar', tone: 'secondary' },
    {
      id: 'toggle',
      label: (row: any) => (row.activo ? 'Desactivar' : 'Activar'),
      tone: (row: any) => (row.activo ? 'danger' : 'success'),
    },
  ];

  conceptos = signal<Concepto[]>([]);
  form: ConceptoRequest = this.emptyForm();
  editingId: number | null = null;
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly tesoreriaApi: TesoreriaApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.tesoreriaApi.listarConceptos().subscribe({
      next: (conceptos) => {
        this.conceptos.set(conceptos);
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

  save(): void {
    this.message.set('');
    this.error.set('');
    const operation =
      this.editingId === null
        ? this.tesoreriaApi.crearConcepto(this.form)
        : this.tesoreriaApi.actualizarConcepto(this.editingId, this.form);

    operation.subscribe({
      next: () => {
        this.message.set(
          this.editingId === null ? 'Concepto registrado.' : 'Concepto actualizado.',
        );
        setTimeout(() => this.message.set(''), 3000);
        this.reset();
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  handleAction(event: TableActionEvent): void {
    const concepto = event.row as Concepto;

    if (event.action.id === 'edit') {
      this.editingId = concepto.idConcepto;
      this.form = {
        nombreConcepto: concepto.nombreConcepto,
        descripcion: concepto.descripcion,
        tipoCobro: concepto.tipoCobro,
        periodicidad: concepto.periodicidad,
        montoFijo: concepto.montoFijo,
        costoTotalProrrateo: concepto.costoTotalProrrateo,
        activo: concepto.activo,
      };
      return;
    }

    this.tesoreriaApi.cambiarEstadoConcepto(concepto.idConcepto, !concepto.activo).subscribe({
      next: () => {
        this.message.set('Estado del concepto actualizado.');
        setTimeout(() => this.message.set(''), 3000);
        this.load();
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  reset(): void {
    this.editingId = null;
    this.form = this.emptyForm();
  }

  private emptyForm(): ConceptoRequest {
    return {
      nombreConcepto: '',
      descripcion: null,
      tipoCobro: 'FIJO',
      periodicidad: 'MENSUAL',
      montoFijo: null,
      costoTotalProrrateo: null,
      activo: true,
    };
  }
}
