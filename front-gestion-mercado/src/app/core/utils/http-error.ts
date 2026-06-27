import { HttpErrorResponse } from '@angular/common/http';

interface ErrorPayload {
  message?: string;
  error?: string;
}

export function httpErrorMessage(error: unknown): string {
  if (error instanceof HttpErrorResponse) {
    const payload = error.error as ErrorPayload | string | null;

    if (typeof payload === 'string' && payload.trim()) {
      return cleanErrorMessage(payload, error.status);
    }

    if (payload && typeof payload === 'object') {
      if (payload.message) {
        return cleanErrorMessage(payload.message, error.status);
      }

      if (payload.error) {
        return cleanErrorMessage(payload.error, error.status);
      }
    }

    if (error.status === 0) {
      return 'No se pudo conectar con el servidor.';
    }

    if (error.status === 401) {
      return 'Tu sesion expiro o no es valida. Vuelve a iniciar sesion.';
    }

    if (error.status === 403) {
      return 'No tienes permisos para realizar esta accion.';
    }

    if (error.status === 400) {
      return 'Revisa los datos ingresados e intenta nuevamente.';
    }

    if (error.status === 404) {
      return 'Registro no encontrado.';
    }

    if (error.status >= 500) {
      return 'Ocurrio un error en el servidor. Intenta nuevamente.';
    }

    return `Error ${error.status}: no se pudo completar la operacion.`;
  }

  return 'No se pudo completar la operacion.';
}

function cleanErrorMessage(message: string, status: number): string {
  const extracted = extractNestedBackendMessage(message);
  if (extracted) {
    return extracted;
  }

  if (isGenericHttpMessage(message)) {
    return fallbackMessage(status);
  }

  if (looksLikeTechnicalTrace(message)) {
    return fallbackMessage(status);
  }

  return message;
}

function extractNestedBackendMessage(message: string): string | null {
  const jsonCandidates = message.match(/\{[^{}]*"message"\s*:\s*"[^"]+"[^{}]*\}/g);
  const lastCandidate = jsonCandidates?.at(-1);

  if (!lastCandidate) {
    return null;
  }

  try {
    const payload = JSON.parse(lastCandidate) as ErrorPayload;
    return payload.message?.trim() || payload.error?.trim() || null;
  } catch {
    return null;
  }
}

function looksLikeTechnicalTrace(message: string): boolean {
  return (
    message.includes(' during [') ||
    message.includes('Feign') ||
    message.includes('http://') ||
    message.includes('https://') ||
    message.includes('Exception') ||
    message.includes('Trace')
  );
}

function isGenericHttpMessage(message: string): boolean {
  const normalized = message.trim().toLowerCase();
  return [
    'bad request',
    'unauthorized',
    'forbidden',
    'not found',
    'conflict',
    'internal server error',
    'service unavailable',
  ].includes(normalized);
}

function fallbackMessage(status: number): string {
  if (status === 0) return 'No se pudo conectar con el servidor.';
  if (status === 401) return 'Tu sesion expiro o no es valida. Vuelve a iniciar sesion.';
  if (status === 403) return 'No tienes permisos para realizar esta accion.';
  if (status === 400) return 'Revisa los datos ingresados e intenta nuevamente.';
  if (status === 404) return 'Registro no encontrado.';
  if (status === 409) return 'La operacion genera un conflicto con datos existentes.';
  if (status >= 500) return 'Ocurrio un error en el servidor. Intenta nuevamente.';
  return 'No se pudo completar la operacion.';
}
