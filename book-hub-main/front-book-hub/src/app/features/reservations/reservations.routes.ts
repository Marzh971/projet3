import { BreadcrumbItem } from '../../shared/components/breadcrumb/breadcrumb.model';

export const RESERVATIONS_ROUTES = [
  {
    path: '',
    loadComponent: () =>
      import('./templates/reservations-list/reservations-list.page').then((m) => m.ReservationsListPage),
    data: {
      breadcrumb: [{ label: 'Reservations', route: '/reservations' }] as BreadcrumbItem[],
    },
  },
];
