import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';
import { ROLE_GROUPS } from './core/constants/roles';
import { AppShell } from './layout/app-shell/app-shell';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login/login').then((m) => m.Login),
  },
  {
    path: '',
    component: AppShell,
    canActivate: [authGuard],
    canActivateChild: [authGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      {
        path: 'dashboard',
        data: { roles: ROLE_GROUPS.all },
        loadComponent: () =>
          import('./features/dashboard/pages/dashboard/dashboard').then((m) => m.Dashboard),
      },
      {
        path: 'patrimonio/socios',
        data: { roles: ROLE_GROUPS.patrimonio },
        loadComponent: () =>
          import('./features/patrimonio/pages/socios/socios').then((m) => m.Socios),
      },
      {
        path: 'patrimonio/puestos',
        data: { roles: ROLE_GROUPS.patrimonio },
        loadComponent: () =>
          import('./features/patrimonio/pages/puestos/puestos').then((m) => m.Puestos),
      },
      {
        path: 'patrimonio/contratos',
        data: { roles: ROLE_GROUPS.patrimonio },
        loadComponent: () =>
          import('./features/patrimonio/pages/contratos/contratos').then((m) => m.Contratos),
      },
      {
        path: 'patrimonio/transferencias',
        data: { roles: ROLE_GROUPS.patrimonio },
        loadComponent: () =>
          import('./features/patrimonio/pages/transferencias/transferencias').then(
            (m) => m.Transferencias,
          ),
      },
      {
        path: 'tesoreria/conceptos',
        data: { roles: ROLE_GROUPS.tesoreria },
        loadComponent: () =>
          import('./features/tesoreria/pages/conceptos/conceptos').then((m) => m.Conceptos),
      },
      {
        path: 'tesoreria/cuotas',
        data: { roles: ROLE_GROUPS.tesoreria },
        loadComponent: () =>
          import('./features/tesoreria/pages/cuotas/cuotas').then((m) => m.Cuotas),
      },
      {
        path: 'tesoreria/estado-cuenta',
        data: { roles: ROLE_GROUPS.estadoCuenta },
        loadComponent: () =>
          import('./features/tesoreria/pages/estado-cuenta/estado-cuenta').then(
            (m) => m.EstadoCuenta,
          ),
      },
      {
        path: 'tesoreria/caja',
        data: { roles: ROLE_GROUPS.tesoreria },
        loadComponent: () => import('./features/tesoreria/pages/caja/caja').then((m) => m.Caja),
      },
      {
        path: 'tesoreria/pagos',
        data: { roles: ROLE_GROUPS.tesoreria },
        loadComponent: () => import('./features/tesoreria/pages/pagos/pagos').then((m) => m.Pagos),
      },
      {
        path: 'tesoreria/comprobantes',
        data: { roles: ROLE_GROUPS.tesoreria },
        loadComponent: () =>
          import('./features/tesoreria/pages/comprobantes/comprobantes').then(
            (m) => m.Comprobantes,
          ),
      },
      {
        path: 'auditoria/eventos',
        data: { roles: ROLE_GROUPS.auditoriaReportes },
        loadComponent: () =>
          import('./features/auditoria-reportes/pages/eventos/eventos').then((m) => m.Eventos),
      },
      {
        path: 'auditoria/anulaciones',
        data: { roles: ROLE_GROUPS.auditoriaReportes },
        loadComponent: () =>
          import('./features/auditoria-reportes/pages/anulaciones/anulaciones').then(
            (m) => m.Anulaciones,
          ),
      },
      {
        path: 'reportes',
        data: { roles: ROLE_GROUPS.auditoriaReportes },
        loadComponent: () =>
          import('./features/auditoria-reportes/pages/reportes/reportes').then((m) => m.Reportes),
      },
      {
        path: 'administracion/usuarios',
        data: { roles: ROLE_GROUPS.adminOnly },
        loadComponent: () =>
          import('./features/administracion/pages/usuarios/usuarios').then((m) => m.Usuarios),
      },
    ],
  },
  { path: '**', redirectTo: 'dashboard' },
];
