import { Component, inject, output } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { NavItem } from './header.model';
import { AuthService } from '../../../features/auth/services/auth.service';
import { ToastService } from '../../services/toast';
import { ButtonComponent } from '../../components/button/button.component';
import { NgOptimizedImage } from '@angular/common';
import { DropdownComponent, DropdownItem } from '../../components/dropdown/dropdown.component';
import { CurrentUser } from '../../../features/auth/models/current-user.model';
import { Roles } from '../../../features/auth/models/roles.model';

@Component({
  selector: 'bh-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, ButtonComponent, NgOptimizedImage, DropdownComponent],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  private readonly authService = inject(AuthService);

  readonly isAuthenticated = this.authService.isAuthenticated;
  readonly currentUser = this.authService.currentUser;
  readonly toast = inject(ToastService);
  readonly router = inject(Router);

  readonly navAuth: NavItem[] = [
    { label: 'Notre collection', route: '/book' },
  ];

  readonly navVisi: NavItem[] = [
    { label: "S'enregistrer", route: '/auth/register' },
    { label: 'Connection', route: '/auth/login' },
  ];

  toggled = output<void>();

  onLogout(): void {
    this.authService.logout().subscribe();
    this.toast.success('Vous êtes déconnecté');
  }

  actionsFor(user: CurrentUser | null): DropdownItem[] {
    if (!user) return [];

    const items: DropdownItem[] = [
      {
        label: 'Voir mon profil',
        icon: 'heroEye',
        action: () => this.router.navigate(['/auth/me']),
      },
    ];

    if (this.authService.hasRole(Roles.LIBRARIAN)) {
      items.push({
        label: 'Dashboard',
        icon: 'heroPencil',
        action: () => this.router.navigate(['/admin/dashboard']),
      });
    }

    return items;
  }
}
