import { BreadcrumbItem } from '../../shared/components/breadcrumb/breadcrumb.model';

export const ADMIN_ROUTES = [
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./templates/admin-dashboard/admin-dashboard.page').then((m) => m.AdminDashboardPage),
    data: {
      breadcrumb: [{ label: 'Dashboard', route: '/admin' }] as BreadcrumbItem[],
    },
  },
  {
    path: 'dashboard/users',
    loadComponent: () =>
      import('../user/templates/user-list/user-list.page').then((m) => m.UserListPage),
    data: {
      breadcrumb: [
        { label: 'Dashboard', route: '/admin/dashboard' },
        { label: 'Utilisateurs', route: '/admin/dashboard/users' },
      ] as BreadcrumbItem[],
    },
  },
  {
    path: 'dashboard/author',
    loadComponent: () =>
      import('../author/templates/author-list/author-list.page').then((m) => m.AuthorListPage),
    data: {
      breadcrumb: [
        { label: 'Dashboard', route: '/admin/dashboard' },
        { label: 'Auteur', route: '/admin/dashboard/author' },
      ] as BreadcrumbItem[],
    },
  },
  {
    path: 'dashboard/publisher',
    loadComponent: () =>
      import('../publisher/templates/publisher-list/publisher-list.page').then(
        (m) => m.PublisherListPage,
      ),
    data: {
      breadcrumb: [
        { label: 'Dashboard', route: '/admin/dashboard' },
        { label: 'Éditeurs', route: '/admin/dashboard/publisher' },
      ] as BreadcrumbItem[],
    },
  },
  {
    path: 'dashboard/categories',
    loadComponent: () =>
      import('../category/templates/category-list/category-list.page').then(
        (m) => m.CategoryListPage,
      ),
    data: {
      breadcrumb: [
        { label: 'Dashboard', route: '/admin/dashboard' },
        { label: 'Catégories', route: '/admin/dashboard/categories' },
      ] as BreadcrumbItem[],
    },
  },
  {
    path: 'dashboard/books',
    loadComponent: () =>
      import('../book/templates/book-list/book-list.page').then((m) => m.BookListPage),
    data: {
      breadcrumb: [
        { label: 'Dashboard', route: '/admin/dashboard' },
        { label: 'Livres', route: '/admin/dashboard/books' },
      ] as BreadcrumbItem[],
    },
  },
];
