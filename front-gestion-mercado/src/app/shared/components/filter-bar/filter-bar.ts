import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

export interface FilterOption {
  label: string;
  value: string;
}

@Component({
  selector: 'app-filter-bar',
  imports: [FormsModule],
  templateUrl: './filter-bar.html',
  styleUrl: './filter-bar.css',
})
export class FilterBar {
  @Input() label = 'Filtro';
  @Input() options: FilterOption[] = [];
  @Output() changed = new EventEmitter<string>();
  value = '';
}
