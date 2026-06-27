import { Component, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { TableActionEvent, TableColumn } from '../../../../core/models/table.models';
import { Socio, SocioRequest } from '../../../../core/models/patrimonio.models';
import { PatrimonioApi } from '../../../../core/services/patrimonio-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-socios',
  imports: [FormsModule, PageHeader, DataTable],
  templateUrl: './socios.html',
  styleUrl: './socios.css',
})
export class Socios {
  readonly columns: TableColumn[] = [
    { key: 'idSocio', label: 'ID' },
    { key: 'dni', label: 'DNI' },
    { key: 'ruc', label: 'RUC' },
    { key: 'nombres', label: 'Nombres' },
    { key: 'apellidos', label: 'Apellidos' },
    { key: 'telefono', label: 'Telefono' },
    { key: 'correo', label: 'Correo' },
    { key: 'direccion', label: 'Direccion' },
    { key: 'estado', label: 'Estado', type: 'status' },
    { key: 'esAsociacion', label: 'Asociacion', type: 'boolean' },
    { key: 'fechaRegistro', label: 'Fecha registro', type: 'date' },
  ];

  readonly actions = [{ id: 'edit', label: 'Editar', tone: 'secondary' as const }];

  showForm = false;
  socios = signal<Socio[]>([]);
  searchTerm = signal('');

  filteredSocios = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) return this.socios();
    
    return this.socios().filter(s => 
      s.dni.toLowerCase().includes(term) ||
      s.nombres.toLowerCase().includes(term) ||
      s.apellidos.toLowerCase().includes(term)
    );
  });

  form: SocioRequest = this.emptyForm();
  editingId: number | null = null;
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly patrimonioApi: PatrimonioApi) {
    this.load();
  }

  toggleForm() {
    this.showForm = !this.showForm;
    if (!this.showForm) {
      this.reset();
    }
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    this.patrimonioApi.listarSocios().subscribe({
      next: (socios) => {
        this.socios.set(socios);
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
    const request = { ...this.form };
    const operation =
      this.editingId === null
        ? this.patrimonioApi.crearSocio(request)
        : this.patrimonioApi.actualizarSocio(this.editingId, request);

    operation.subscribe({
      next: () => {
        this.message.set(this.editingId === null ? 'Socio registrado.' : 'Socio actualizado.');
        this.reset();
        this.showForm = false;
        this.load();

        setTimeout(() => {
          this.message.set('');
        }, 3000);
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  handleAction(event: TableActionEvent): void {
    if (event.action.id !== 'edit') {
      return;
    }

    const socio = event.row as Socio;
    this.editingId = socio.idSocio;
    this.form = {
      dni: socio.dni,
      ruc: socio.ruc,
      nombres: socio.nombres,
      apellidos: socio.apellidos,
      telefono: socio.telefono,
      correo: socio.correo,
      direccion: socio.direccion,
      estado: socio.estado,
      esAsociacion: socio.esAsociacion,
    };
    this.showForm = true;
  }

  reset(): void {
    this.editingId = null;
    this.form = this.emptyForm();
  }

  private emptyForm(): SocioRequest {
    return {
      dni: '',
      ruc: null,
      nombres: '',
      apellidos: '',
      telefono: null,
      correo: null,
      direccion: null,
      estado: 'ACTIVO',
      esAsociacion: false,
    };
  }

  formatError(err: string): string {
    if (!err) return '';
    const parts = err.split(',').map(s => s.trim()).filter(s => s && !s.toLowerCase().match(/^id\b/));
    return parts.length > 0 ? parts.join(', ') : err;
  }
}
