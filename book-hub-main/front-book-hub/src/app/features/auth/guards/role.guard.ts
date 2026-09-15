import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { map } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Role } from '../models/roles.model';

export const roleGuard =
  (...allowedRoles: Role[]): CanActivateFn =>
  () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const checkRoles = () => {
      if (allowedRoles.some((role) => authService.hasRole(role))) {
        return true;
      }
      router.navigate(['/error/unauthenticated']);
      return false;
    };

    if (authService.isAuthenticated()) {
      return checkRoles();
    }

    return authService.restoreSession().pipe(
      map((user) => {
        if (!user) {
          router.navigate(['/auth/login']);
          return false;
        }
        return checkRoles();
      }),
    );
  };
