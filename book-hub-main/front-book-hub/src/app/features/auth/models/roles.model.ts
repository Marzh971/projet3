export const Roles = {
  USER: 'ROLE_USER',
  LIBRARIAN: 'ROLE_LIBRARIAN',
  ADMIN: 'ROLE_ADMIN',
} as const;

export type Role = (typeof Roles)[keyof typeof Roles];
