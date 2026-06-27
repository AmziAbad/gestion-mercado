import { Component, EventEmitter, Input, Output } from '@angular/core';

import { Modal } from '../modal/modal';

@Component({
  selector: 'app-confirm-dialog',
  imports: [Modal],
  templateUrl: './confirm-dialog.html',
  styleUrl: './confirm-dialog.css',
})
export class ConfirmDialog {
  @Input() open = false;
  @Input() title = 'Confirmar accion';
  @Input() message = '';
  @Output() cancelled = new EventEmitter<void>();
  @Output() confirmed = new EventEmitter<void>();
}
