import { BreadcrumbItem } from '../../shared/components/breadcrumb/breadcrumb.model';

export const LOAN_ROUTES = [
  {
    path: '',
    loadComponent: () => import('./templates/loan-list/loan-list.page').then((m) => m.LoanListPage),
    data: {
      breadcrumb: [{ label: 'Loan', route: '/loan' }] as BreadcrumbItem[],
    },
  },
];
