import { Routes } from '@angular/router';

export const HOME_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./templates/home.page').then((m) => m.HomePage),
  },
];
