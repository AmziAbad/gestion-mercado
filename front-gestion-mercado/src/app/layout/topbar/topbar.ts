import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthApi } from '../../core/services/auth-api';
import { Session } from '../../core/services/session';

@Component({
  selector: 'app-topbar',
  imports: [],
  templateUrl: './topbar.html',
  styleUrl: './topbar.css',
})
export class Topbar {
  private readonly authApi = inject(AuthApi);
  private readonly router = inject(Router);
  private readonly session = inject(Session);

  readonly user = this.session.user;

  logout(): void {
    this.authApi.logout();
    void this.router.navigate(['/login']);
  }
}
