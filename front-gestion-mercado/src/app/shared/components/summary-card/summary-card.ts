import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-summary-card',
  imports: [],
  templateUrl: './summary-card.html',
  styleUrl: './summary-card.css',
})
export class SummaryCard {
  @Input() label = '';
  @Input() value: string | number = '-';
  @Input() helper = '';
}
