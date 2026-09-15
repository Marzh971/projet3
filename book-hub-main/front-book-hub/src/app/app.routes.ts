import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./features/home/home.routes').then((m) => m.HOME_ROUTES),
  },
  {
    path: 'admin',
    loadChildren: () => import('./features/admin/admin.routes').then((m) => m.ADMIN_ROUTES),

  },
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'author',
    loadChildren: () =>
      import('./features/author/author.routes').then((m) => m.AUTHOR_ROUTES),
  },
  {
    path: 'book',
    loadChildren: () =>
      import('./features/book/book.routes').then((m) => m.BOOK_ROUTES),
  },
  {
    path: 'category',
    loadChildren: () =>
      import('./features/category/category.routes').then((m) => m.CATEGORY_ROUTES),
  },
  {
    path: 'copies',
    loadChildren: () =>
      import('./features/copy/copy.routes').then((m) => m.COPY_ROUTES),
  },
  {
    path: 'error',
    loadChildren: () =>
      import('./features/error/error.route').then((m) => m.ERRORS_ROUTES),
  },
  {
    path: 'loan',
    loadChildren: () =>
      import('./features/loan/loan.routes').then((m) => m.LOAN_ROUTES),
  },
  {
    path: 'publisher',
    loadChildren: () =>
      import('./features/publisher/publisher.routes').then((m) => m.PUBLISHER_ROUTES),
  },
  {
    path: 'reservations',
    loadChildren: () =>
      import('./features/reservations/reservations.routes').then((m) => m.RESERVATIONS_ROUTES),
  },
  {
    path: 'users',
    loadChildren: () =>
      import('./features/user/user.routes').then((m) => m.USERS_ROUTES),
  },
];
