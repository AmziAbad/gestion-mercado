import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { RolUsuario } from '../models/auth.models';
import { Session } from '../services/session';

export const authGuard: CanActivateFn = (route, state) => {
  const session = inject(Session);
  const router = inject(Router);

  if (!session.isAuthenticated()) {
    return router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url },
    });
  }

  const allowedRoles = route.data['roles'] as readonly RolUsuario[] | undefined;
  if (!session.hasAnyRole(allowedRoles)) {
    return router.createUrlTree(['/dashboard']);
  }

  return true;
};
