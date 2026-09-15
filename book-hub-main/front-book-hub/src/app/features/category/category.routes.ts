import { BreadcrumbItem } from '../../shared/components/breadcrumb/breadcrumb.model';

// @ts-ignore
export const CATEGORY_ROUTES = [
  {
    path: '',
    loadComponent: () =>
      import('./templates/category-list/category-list.page').then((m) => m.CategoryListPage),
    data: {
      breadcrumb: [{ label: 'Category', route: '/category' }] as BreadcrumbItem[],
    },
  },
  {
    path: 'create',
    loadComponent: () =>
      import('./templates/category-form/category-form.page').then((m) => m.CategoryFormPage),
    data: {
      breadcrumb: [
        { label: 'Category', route: '/category' },
        { label: 'Create', route: '/category/create' },
      ] as BreadcrumbItem[],
    },
  },
];
