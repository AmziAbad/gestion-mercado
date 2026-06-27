import { Component, Input } from '@angular/core';

import { AppIcon } from '../app-icon/app-icon';

@Component({
  selector: 'app-summary-card',
  imports: [AppIcon],
  templateUrl: './summary-card.html',
  styleUrl: './summary-card.css',
})
export class SummaryCard {
  @Input() label = '';
  @Input() value: string | number = '-';
  @Input() helper = '';
  @Input() icon = '';
}
