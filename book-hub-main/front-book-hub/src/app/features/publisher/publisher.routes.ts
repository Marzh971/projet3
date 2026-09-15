import { BreadcrumbItem } from '../../shared/components/breadcrumb/breadcrumb.model';

export const PUBLISHER_ROUTES = [
  {
    path: '',
    loadComponent: () =>
      import('./templates/publisher-list/publisher-list.page').then((m) => m.PublisherListPage),
    data: {
      breadcrumb: [{ label: 'Publisher', route: '/publisher' }] as BreadcrumbItem[],
    },
  },
  {
    path: 'create',
    loadComponent: () =>
      import('./templates/publisher-form/publisher-form.page').then((m) => m.PublisherFormPage),
    data: {
      breadcrumb: [
        { label: 'Publisher', route: '/publisher' },
        { label: 'Créer', route: '/publisher/create' },
      ] as BreadcrumbItem[],
    },
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./templates/publisher-form/publisher-form.page').then((m) => m.PublisherFormPage),
    data: {
      breadcrumb: [
        { label: 'Publisher', route: '/publisher' },
        { label: 'Modifier', route: '/publisher/edit' },
      ] as BreadcrumbItem[],
    },
  },
];
