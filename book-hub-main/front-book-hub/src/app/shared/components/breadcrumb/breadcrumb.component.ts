import { Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { heroChevronRight, heroHome } from '@ng-icons/heroicons/outline';
import { filter } from 'rxjs';

import { BreadcrumbService } from '../../services/breadcrumb';

@Component({
  selector: 'bh-breadcrumb',
  standalone: true,
  imports: [RouterLink, NgIcon],
  providers: [
    provideIcons({
      heroChevronRight,
      heroHome,
    }),
  ],
  templateUrl: './breadcrumb.component.html',
  styleUrl: './breadcrumb.component.css',
})
export class BreadcrumbComponent {
  private readonly router = inject(Router);

  breadcrumb = inject(BreadcrumbService);

  /**
   * Indique si nous sommes sur la page d'accueil.
   */
  isHomePage = false;

  constructor() {
    this.isHomePage = this.router.url === '/';

    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isHomePage = event.urlAfterRedirects === '/';
      });
  }
}
