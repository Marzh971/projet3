import { BreadcrumbItem } from '../../shared/components/breadcrumb/breadcrumb.model';


export const BOOK_ROUTES = [
  {
    path: '',
    loadComponent: () => import('./templates/book-list/book-list.page').then((m) => m.BookListPage),
    data: {
      breadcrumb: [{ label: 'Book', route: '/book' }] as BreadcrumbItem[],
    },
  },
  {
    path: 'create',
    loadComponent: () => import('./templates/book-form/book-form.page').then((m) => m.BookFormPage),
    data: {
      breadcrumb: [
        { label: 'Book', route: '/book' },
        { label: 'Create', route: '' },
      ] as BreadcrumbItem[],
    },
  },
  {
    path: ':isbn',
    loadComponent: () =>
      import('./templates/book-detail/book-detail.page').then((m) => m.BookDetailPage),
    data: {
      breadcrumb: [
        { label: 'Book', route: '/book' },
        { label: 'Detail', route: '/book/:isbn' },
      ] as BreadcrumbItem[],
    },
  },
];
