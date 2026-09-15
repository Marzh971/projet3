import { AuthLoginFormComponent } from './templates/auth-login-form/auth-login-form';
import { AuthRegisterFormComponent } from './templates/auth-register-form/auth-register-form';

export const AUTH_ROUTES = [
  {
    path: 'login',
    component: AuthLoginFormComponent,
  },
  {
    path: 'register',
    component: AuthRegisterFormComponent,
  },
  {
    path: 'me',
    loadComponent: () =>
      import('../user/templates/user-detail/user-detail.page').then((m) => m.UserDetailPage),
  },
];
