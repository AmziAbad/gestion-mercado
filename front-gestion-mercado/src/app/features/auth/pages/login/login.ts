import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { APP_ROUTES } from '../../../../core/constants/app-routes';
import { LoginRequest } from '../../../../core/models/auth.models';
import { AuthApi } from '../../../../core/services/auth-api';
import { Session } from '../../../../core/services/session';
import { httpErrorMessage } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  readonly credentials: LoginRequest = {
    username: '',
    password: '',
  };

  loading = signal(false);
  error = signal('');

  constructor(
    private readonly authApi: AuthApi,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly session: Session,
  ) {
    if (this.session.isAuthenticated()) {
      void this.router.navigateByUrl(APP_ROUTES.dashboard);
    }
  }

  login(): void {
    this.error.set('');
    this.loading.set(true);

    this.authApi.login(this.credentials).subscribe({
      next: () => {
        const returnUrl =
          this.route.snapshot.queryParamMap.get('returnUrl') ?? APP_ROUTES.dashboard;
        void this.router.navigateByUrl(returnUrl);
      },
      error: (error: unknown) => {
        this.error.set(httpErrorMessage(error));
        this.loading.set(false);
      },
      complete: () => {
        this.loading.set(false);
      },
    });
  }
}
