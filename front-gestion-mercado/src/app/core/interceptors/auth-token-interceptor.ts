import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { Session } from '../services/session';

export const authTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(Session).token();

  if (!token || req.url.includes('/auth/login')) {
    return next(req);
  }

  return next(
    req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    }),
  );
};
