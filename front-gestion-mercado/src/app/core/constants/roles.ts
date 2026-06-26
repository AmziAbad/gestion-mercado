import { RolUsuario } from '../models/auth.models';

interface RoleGroups {
  all: readonly RolUsuario[];
  adminOnly: readonly RolUsuario[];
  patrimonio: readonly RolUsuario[];
  patrimonioRead: readonly RolUsuario[];
  tesoreria: readonly RolUsuario[];
  estadoCuenta: readonly RolUsuario[];
  auditoriaReportes: readonly RolUsuario[];
}

export const ROLE_GROUPS: RoleGroups = {
  all: ['ADMIN', 'TESORERO', 'RECEPCIONISTA'],
  adminOnly: ['ADMIN'],
  patrimonio: ['ADMIN', 'RECEPCIONISTA'],
  patrimonioRead: ['ADMIN', 'TESORERO', 'RECEPCIONISTA'],
  tesoreria: ['ADMIN', 'TESORERO'],
  estadoCuenta: ['ADMIN', 'TESORERO', 'RECEPCIONISTA'],
  auditoriaReportes: ['ADMIN', 'TESORERO'],
};
