import { APP_ROUTES } from './app-routes';
import { ROLE_GROUPS } from './roles';
import { RolUsuario } from '../models/auth.models';

export interface NavigationLink {
  label: string;
  route: string;
  icon: string;
  roles: readonly RolUsuario[];
}

export interface NavigationGroup {
  label: string;
  links: NavigationLink[];
}

export const NAVIGATION_GROUPS: NavigationGroup[] = [
  {
    label: 'Inicio',
    links: [
      {
        label: 'Panel general',
        route: APP_ROUTES.dashboard,
        icon: 'dashboard',
        roles: ROLE_GROUPS.all,
      },
    ],
  },
  {
    label: 'Patrimonio',
    links: [
      { label: 'Socios', route: APP_ROUTES.socios, icon: 'socios', roles: ROLE_GROUPS.patrimonio },
      {
        label: 'Puestos',
        route: APP_ROUTES.puestos,
        icon: 'puestos',
        roles: ROLE_GROUPS.patrimonio,
      },
      {
        label: 'Contratos',
        route: APP_ROUTES.contratos,
        icon: 'contratos',
        roles: ROLE_GROUPS.patrimonio,
      },
      {
        label: 'Transferencias',
        route: APP_ROUTES.transferencias,
        icon: 'transferencias',
        roles: ROLE_GROUPS.patrimonio,
      },
    ],
  },
  {
    label: 'Tesoreria',
    links: [
      {
        label: 'Conceptos',
        route: APP_ROUTES.conceptos,
        icon: 'conceptos',
        roles: ROLE_GROUPS.tesoreria,
      },
      { label: 'Cuotas', route: APP_ROUTES.cuotas, icon: 'cuotas', roles: ROLE_GROUPS.tesoreria },
      {
        label: 'Estado de cuenta',
        route: APP_ROUTES.estadoCuenta,
        icon: 'estado-cuenta',
        roles: ROLE_GROUPS.estadoCuenta,
      },
      { label: 'Caja', route: APP_ROUTES.caja, icon: 'caja', roles: ROLE_GROUPS.tesoreria },
      { label: 'Pagos', route: APP_ROUTES.pagos, icon: 'pagos', roles: ROLE_GROUPS.tesoreria },
      {
        label: 'Comprobantes',
        route: APP_ROUTES.comprobantes,
        icon: 'comprobantes',
        roles: ROLE_GROUPS.tesoreria,
      },
    ],
  },
  {
    label: 'Auditoria y reportes',
    links: [
      {
        label: 'Eventos',
        route: APP_ROUTES.auditoriaEventos,
        icon: 'eventos',
        roles: ROLE_GROUPS.auditoriaReportes,
      },
      {
        label: 'Anulaciones',
        route: APP_ROUTES.auditoriaAnulaciones,
        icon: 'anulaciones',
        roles: ROLE_GROUPS.auditoriaReportes,
      },
      {
        label: 'Reportes',
        route: APP_ROUTES.reportes,
        icon: 'reportes',
        roles: ROLE_GROUPS.auditoriaReportes,
      },
    ],
  },
  {
    label: 'Administracion',
    links: [
      {
        label: 'Usuarios',
        route: APP_ROUTES.usuarios,
        icon: 'usuarios',
        roles: ROLE_GROUPS.adminOnly,
      },
    ],
  },
];
