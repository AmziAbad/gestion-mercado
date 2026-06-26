import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-detail-panel',
  imports: [],
  templateUrl: './detail-panel.html',
  styleUrl: './detail-panel.css',
})
export class DetailPanel {
  @Input() title = '';
}
