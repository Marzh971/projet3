import { Component, inject, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Header } from './shared/shell/header/header';
import { ToastComponent } from './shared/components/toast/toast.component';
import { ModalComponent } from './shared/components/modal/modal.component';
import { Footer } from './shared/shell/footer/footer';
import { BreadcrumbComponent } from './shared/components/breadcrumb/breadcrumb.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, ToastComponent, ModalComponent, Footer, BreadcrumbComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('front-book-hub');
  private readonly router = inject(Router);

  protected get isHomePage(): boolean {
    return this.router.url === '/';
  }
}
