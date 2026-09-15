import { Component, inject } from '@angular/core';
import { SectionComponent } from '../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-error-unauthenticated',
  standalone: true,
  templateUrl: './error-unauthenticated.page.html',
  imports: [SectionComponent, ButtonComponent],
})
export class ErrorUnauthenticatedPage {
  router = inject(Router);

  navigateToHome() {
    this.router.navigate(['/']);
  }
}
