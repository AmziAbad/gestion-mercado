import { HttpErrorResponse } from '@angular/common/http';

export function httpErrorMessage(error: unknown): string {
  if (error instanceof HttpErrorResponse) {
    const payload = error.error as { message?: string; error?: string } | string | null;

    if (typeof payload === 'string' && payload.trim()) {
      return payload;
    }

    if (payload && typeof payload === 'object') {
      if (payload.message) {
        return payload.message;
      }

      if (payload.error) {
        return payload.error;
      }
    }

    return `Error ${error.status}: no se pudo completar la operacion.`;
  }

  return 'No se pudo completar la operacion.';
}
