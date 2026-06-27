import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-icon',
  imports: [],
  templateUrl: './app-icon.html',
  styleUrl: './app-icon.css',
})
export class AppIcon {
  @Input() name = 'circle';
  @Input() size = 18;
}
