import { Routes } from '@angular/router';

export const COPY_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./templates/copy-list/copy-list.page').then((m) => m.CopyListPage),
  },
  {
    path: 'create',
    loadComponent: () => import('./templates/copy-form/copy-form.page').then((m) => m.CopyFormPage),
  },
  {
    path: ':id',
    loadComponent: () =>
      import('./templates/copy-detail/copy-detail.page').then((m) => m.CopyDetailPage),
  },
];
