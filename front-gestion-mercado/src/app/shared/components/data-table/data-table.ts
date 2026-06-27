import { Component, EventEmitter, Input, Output } from '@angular/core';

import { TableAction, TableActionEvent, TableColumn } from '../../../core/models/table.models';
import { StatusBadge } from '../status-badge/status-badge';

@Component({
  selector: 'app-data-table',
  imports: [StatusBadge],
  templateUrl: './data-table.html',
  styleUrl: './data-table.css',
})
export class DataTable {
  @Input() columns: TableColumn[] = [];
  @Input() rows: unknown[] = [];
  @Input() actions: TableAction[] = [];
  @Input() emptyMessage = 'No hay registros para mostrar.';
  @Output() actionSelected = new EventEmitter<TableActionEvent>();

  value(row: unknown, key: string): unknown {
    return key.split('.').reduce<unknown>((current, part) => {
      if (current && typeof current === 'object' && part in current) {
        return (current as Record<string, unknown>)[part];
      }

      return null;
    }, row);
  }

  display(row: unknown, column: TableColumn): string {
    const value = this.value(row, column.key);

    if (value === null || value === undefined || value === '') {
      return '-';
    }

    if (column.type === 'currency') {
      return new Intl.NumberFormat('es-PE', {
        style: 'currency',
        currency: 'PEN',
      }).format(Number(value));
    }

    if (column.type === 'date') {
      let dateString = String(value);
      if (dateString.length === 10) {
        dateString += 'T12:00:00';
      }
      return new Intl.DateTimeFormat('es-PE').format(new Date(dateString));
    }

    if (column.type === 'boolean') {
      return value ? 'Si' : 'No';
    }

    return String(value);
  }

  emitAction(action: TableAction, row: unknown): void {
    this.actionSelected.emit({ action, row });
  }

  getActionLabel(action: TableAction, row: unknown): string {
    return typeof action.label === 'function' ? action.label(row) : action.label;
  }

  getActionTone(action: TableAction, row: unknown): string {
    const tone = typeof action.tone === 'function' ? action.tone(row) : action.tone;
    return tone ? `table-action--${tone}` : '';
  }
}
