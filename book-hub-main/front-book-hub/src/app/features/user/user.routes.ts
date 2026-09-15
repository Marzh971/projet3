import { BreadcrumbItem } from '../../shared/components/breadcrumb/breadcrumb.model';
import { roleGuard } from '../auth/guards/role.guard';
import { Roles } from '../auth/models/roles.model';

export const USERS_ROUTES = [
  {
    path: '',
    canActivate: [roleGuard(Roles.ADMIN)],
    loadComponent: () => import('./templates/user-list/user-list.page').then((m) => m.UserListPage),
    data: {
      breadcrumb: [{ label: 'Users', route: '/users' }] as BreadcrumbItem[],
    },
  },
  {
    path: 'create',
    canActivate: [roleGuard(Roles.ADMIN)],
    loadComponent: () => import('./templates/user-form/user-form.page').then((m) => m.UserFormPage),
    data: {
      breadcrumb: [
        { label: 'Users', route: '/users' },
        { label: 'Create', route: '/users/create' },
      ] as BreadcrumbItem[],
    },
  },
  {
    path: ':id',
    canActivate: [roleGuard(Roles.USER)],
    loadComponent: () =>
      import('./templates/user-detail/user-detail.page').then((m) => m.UserDetailPage),
    data: {
      breadcrumb: [
        { label: 'Users', route: '/users' },
        { label: 'Detail', route: '/users/:id' },
      ] as BreadcrumbItem[],
    },
  },
];
