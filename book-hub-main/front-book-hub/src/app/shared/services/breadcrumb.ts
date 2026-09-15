import { Injectable, signal, inject } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter } from 'rxjs/operators';
import { BreadcrumbItem } from '../components/breadcrumb/breadcrumb.model';


@Injectable({ providedIn: 'root' })
export class BreadcrumbService {
  items = signal<BreadcrumbItem[]>([]);

  private router = inject(Router);
  private route  = inject(ActivatedRoute);

  constructor() {
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd)
    ).subscribe(() => {
      this.items.set(this.collectBreadcrumb(this.route.root));

      const fragment = this.router.parseUrl(this.router.url).fragment;
      if (fragment) {
        setTimeout(() => {
          const target = document.getElementById(fragment);
          const container = document.querySelector('.page-content');
          if (target && container) {
            container.scrollTo({ top: target.offsetTop - window.innerHeight * 0.12, behavior: 'smooth' });
          }
        }, 0);
      }
    });
  }

  private collectBreadcrumb(route: ActivatedRoute): BreadcrumbItem[] {
    let current = route;
    while (current.firstChild) current = current.firstChild;

    const data = current.snapshot.data?.['breadcrumb'];
    return Array.isArray(data) ? data : [];
  }
}
