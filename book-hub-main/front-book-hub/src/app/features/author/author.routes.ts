import { BreadcrumbItem } from '../../shared/components/breadcrumb/breadcrumb.model';

export const AUTHOR_ROUTES = [
  {
    path: '',
    loadComponent: () =>
      import('./templates/author-list/author-list.page').then((m) => m.AuthorListPage),
    data: {
      breadcrumb: [{ label: 'Author', route: '/author' }] as BreadcrumbItem[],
    },
  },
  {
    path: 'create',
    loadComponent: () =>
      import('./templates/author-form/author-form.page').then((m) => m.AuthorFormPage),
    data: {
      breadcrumb: [
        { label: 'Author', route: '/author' },
        { label: 'Créer', route: '/author/create' },
      ] as BreadcrumbItem[],
    },
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./templates/author-form/author-form.page').then((m) => m.AuthorFormPage),
    data: {
      breadcrumb: [
        { label: 'Author', route: '/author' },
        { label: 'Modifier', route: '/author/edit' },
      ] as BreadcrumbItem[],
    },
  },
];
