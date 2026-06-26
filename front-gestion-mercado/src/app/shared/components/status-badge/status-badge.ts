import { Component, Input } from '@angular/core';

import { STATUS_TONES, StatusTone } from '../../../core/constants/status-map';

@Component({
  selector: 'app-status-badge',
  imports: [],
  templateUrl: './status-badge.html',
  styleUrl: './status-badge.css',
})
export class StatusBadge {
  @Input() value: string | number | boolean | null | undefined = '';
  @Input() tone: StatusTone | null = null;

  label(): string {
    if (typeof this.value === 'boolean') {
      return this.value ? 'Activo' : 'Inactivo';
    }

    return String(this.value ?? '-');
  }

  cssClass(): string {
    const key = String(this.value ?? '').toUpperCase();
    const tone = this.tone ?? STATUS_TONES[key] ?? 'neutral';
    return `badge badge--${tone}`;
  }
}
