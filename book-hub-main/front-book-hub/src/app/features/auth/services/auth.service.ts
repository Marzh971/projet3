import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceResponse } from '../../../shared/models/service-response.model';
import { AuthLoginDto } from '../dtos/auth-login.dto';
import { catchError, map, Observable, of, switchMap, tap } from 'rxjs';
import { AuthRegisterDto } from '../dtos/auth-register.dto';
import { CurrentUser } from '../models/current-user.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly _currentUser = signal<CurrentUser | null>(null);

  readonly currentUser = this._currentUser.asReadonly();
  readonly isAuthenticated = computed(() => this._currentUser() !== null);

  login(credentials: AuthLoginDto): Observable<CurrentUser | null> {
    return this.http
      .post<void>('/api/auth/login', credentials)
      .pipe(switchMap(() => this.fetchCurrentUser()));
  }

  register(payload: AuthRegisterDto): Observable<ServiceResponse<CurrentUser | null>> {
    return this.http
      .post<ServiceResponse<CurrentUser>>('/api/auth/register', payload)
      .pipe(map((response) => response));
  }

  logout(): Observable<void> {
    return this.http.delete<void>('/api/auth/logout').pipe(tap(() => this._currentUser.set(null)));
  }

  restoreSession(): Observable<CurrentUser | null> {
    return this.fetchCurrentUser().pipe(
      catchError(() => {
        this._currentUser.set(null);
        return of(null);
      }),
    );
  }

  private fetchCurrentUser(): Observable<CurrentUser | null> {
    return this.http.get<ServiceResponse<CurrentUser>>('/api/auth/me').pipe(
      tap((response) => this._currentUser.set(response.data)),
      map((response) => response.data),
    );
  }

  hasRole(role: string): boolean {
    return this.currentUser()?.roles.includes(role) ?? false;
  }
}
