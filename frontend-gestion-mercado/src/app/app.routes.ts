import { Routes } from '@angular/router';
import { Layout } from './layout/layout';
import { Cobranzas } from './pages/cobranzas/cobranzas';
import { Dashboard } from './pages/dashboard/dashboard';
import { Login } from './pages/login/login';
import { Puestos } from './pages/puestos/puestos';
import { Reportes } from './pages/reportes/reportes';
import { Servicios } from './pages/servicios/servicios';
import { Socios } from './pages/socios/socios';
import { Transferencias } from './pages/transferencias/transferencias';
import { Usuarios } from './pages/usuarios/usuarios';

export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: '',
    component: Layout,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      { path: 'dashboard', component: Dashboard },
      { path: 'socios', component: Socios },
      { path: 'puestos', component: Puestos },
      { path: 'servicios', component: Servicios },
      { path: 'cobranzas', component: Cobranzas },
      { path: 'transferencias', component: Transferencias },
      { path: 'reportes', component: Reportes },
      { path: 'usuarios', component: Usuarios },
    ],
  },
  { path: '**', redirectTo: 'dashboard' },
];
