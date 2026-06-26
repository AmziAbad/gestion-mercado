import { APP_ROUTES } from './app-routes';
import { ROLE_GROUPS } from './roles';
import { RolUsuario } from '../models/auth.models';

export interface NavigationLink {
  label: string;
  route: string;
  roles: readonly RolUsuario[];
}

export interface NavigationGroup {
  label: string;
  links: NavigationLink[];
}

export const NAVIGATION_GROUPS: NavigationGroup[] = [
  {
    label: 'Inicio',
    links: [{ label: 'Panel general', route: APP_ROUTES.dashboard, roles: ROLE_GROUPS.all }],
  },
  {
    label: 'Patrimonio',
    links: [
      { label: 'Socios', route: APP_ROUTES.socios, roles: ROLE_GROUPS.patrimonio },
      { label: 'Puestos', route: APP_ROUTES.puestos, roles: ROLE_GROUPS.patrimonio },
      { label: 'Contratos', route: APP_ROUTES.contratos, roles: ROLE_GROUPS.patrimonio },
      { label: 'Transferencias', route: APP_ROUTES.transferencias, roles: ROLE_GROUPS.patrimonio },
    ],
  },
  {
    label: 'Tesoreria',
    links: [
      { label: 'Conceptos', route: APP_ROUTES.conceptos, roles: ROLE_GROUPS.tesoreria },
      { label: 'Cuotas', route: APP_ROUTES.cuotas, roles: ROLE_GROUPS.tesoreria },
      { label: 'Estado de cuenta', route: APP_ROUTES.estadoCuenta, roles: ROLE_GROUPS.estadoCuenta },
      { label: 'Caja', route: APP_ROUTES.caja, roles: ROLE_GROUPS.tesoreria },
      { label: 'Pagos', route: APP_ROUTES.pagos, roles: ROLE_GROUPS.tesoreria },
      { label: 'Comprobantes', route: APP_ROUTES.comprobantes, roles: ROLE_GROUPS.tesoreria },
    ],
  },
  {
    label: 'Auditoria y reportes',
    links: [
      { label: 'Eventos', route: APP_ROUTES.auditoriaEventos, roles: ROLE_GROUPS.auditoriaReportes },
      {
        label: 'Anulaciones',
        route: APP_ROUTES.auditoriaAnulaciones,
        roles: ROLE_GROUPS.auditoriaReportes,
      },
      { label: 'Reportes', route: APP_ROUTES.reportes, roles: ROLE_GROUPS.auditoriaReportes },
    ],
  },
  {
    label: 'Administracion',
    links: [{ label: 'Usuarios', route: APP_ROUTES.usuarios, roles: ROLE_GROUPS.adminOnly }],
  },
];
