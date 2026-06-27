import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-action-button',
  imports: [],
  templateUrl: './action-button.html',
  styleUrl: './action-button.css',
})
export class ActionButton {
  @Input() label = 'Accion';
  @Input() tone: 'primary' | 'secondary' | 'danger' = 'primary';
  @Input() disabled = false;
  @Output() pressed = new EventEmitter<void>();
}
