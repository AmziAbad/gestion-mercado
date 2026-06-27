import { Component, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PageHeader } from '../../../../layout/page-header/page-header';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { DetailPanel } from '../../../../shared/components/detail-panel/detail-panel';
import { StatusBadge } from '../../../../shared/components/status-badge/status-badge';
import { Puesto, PuestoRequest, Saneamiento } from '../../../../core/models/patrimonio.models';
import { TableActionEvent, TableColumn } from '../../../../core/models/table.models';
import { PatrimonioApi } from '../../../../core/services/patrimonio-api';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-puestos',
  imports: [FormsModule, PageHeader, DataTable, StatusBadge],
  templateUrl: './puestos.html',
  styleUrl: './puestos.css',
})
export class Puestos {
  readonly columns: TableColumn[] = [
    { key: 'idPuesto', label: 'ID' },
    { key: 'codigoPuesto', label: 'Codigo' },
    { key: 'pabellon', label: 'Pabellon' },
    { key: 'medidas', label: 'Medidas' },
    { key: 'giro', label: 'Giro' },
    { key: 'estadoPuesto', label: 'Estado', type: 'status' },
  ];

  readonly actions = [
    { id: 'edit', label: 'Editar', tone: 'secondary' as const },
    { id: 'saneamiento', label: 'Saneamiento', tone: 'secondary' as const },
  ];

  puestos = signal<Puesto[]>([]);
  searchTerm = signal('');

  filteredPuestos = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) return this.puestos();
    
    return this.puestos().filter(p => 
      p.codigoPuesto.toLowerCase().includes(term) ||
      (p.pabellon && p.pabellon.toLowerCase().includes(term)) ||
      (p.giro && p.giro.toLowerCase().includes(term))
    );
  });
  saneamiento = signal<Saneamiento | null>(null);
  form: PuestoRequest = this.emptyForm();
  editingId: number | null = null;
  loading = signal(false);
  message = signal('');
  error = signal('');

  constructor(private readonly patrimonioApi: PatrimonioApi) {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    this.patrimonioApi.listarPuestos().subscribe({
      next: (puestos) => {
        this.puestos.set(puestos);
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
        ? this.patrimonioApi.crearPuesto(this.form)
        : this.patrimonioApi.actualizarPuesto(this.editingId, this.form);

    operation.subscribe({
      next: () => {
        this.message.set(this.editingId === null ? 'Puesto registrado.' : 'Puesto actualizado.');
        this.reset();
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

  saneamientoModalOpen = signal(false);

  handleAction(event: TableActionEvent): void {
    const puesto = event.row as Puesto;

    if (event.action.id === 'edit') {
      this.editingId = puesto.idPuesto;
      this.form = {
        codigoPuesto: puesto.codigoPuesto,
        pabellon: puesto.pabellon,
        medidas: puesto.medidas,
        giro: puesto.giro,
        estadoPuesto: puesto.estadoPuesto,
      };
      return;
    }

    if (event.action.id === 'saneamiento') {
      this.consultarSaneamiento(puesto.idPuesto);
    }
  }

  consultarSaneamiento(idPuesto: number): void {
    this.error.set('');
    this.saneamiento.set(null); // Clear previous state
    this.saneamientoModalOpen.set(true); // Open modal to show loading or just open it
    this.patrimonioApi.consultarSaneamiento(idPuesto).subscribe({
      next: (saneamiento) => {
        this.saneamiento.set(saneamiento);
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
      },
    });
  }

  closeSaneamientoModal(): void {
    this.saneamientoModalOpen.set(false);
  }

  money(value: number): string {
    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'PEN',
    }).format(value);
  }

  reset(): void {
    this.editingId = null;
    this.form = this.emptyForm();
  }

  private emptyForm(): PuestoRequest {
    return {
      codigoPuesto: '',
      pabellon: '',
      medidas: null,
      giro: null,
      estadoPuesto: 'LIBRE',
    };
  }
}
