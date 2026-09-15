
export const ERRORS_ROUTES = [
  {
    path: 'unauthenticated',
    loadComponent: () =>
      import('./templates/error-unauthenticated.page').then((m) => m.ErrorUnauthenticatedPage),
  },
];
