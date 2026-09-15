import { Component, inject, HostListener, OnInit, OnDestroy } from '@angular/core';
import { NgTemplateOutlet } from '@angular/common';
import { Router, NavigationStart } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { ButtonComponent } from '../button/button.component';
import { ModalService } from '../../services/modal';

@Component({
  selector: 'bh-modal',
  standalone: true,
  imports: [NgTemplateOutlet, ButtonComponent],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css'
})
export class ModalComponent implements OnInit, OnDestroy {
  modal  = inject(ModalService);
  router = inject(Router);

  private sub!: Subscription;

  ngOnInit() {
    this.sub = this.router.events.pipe(
      filter(e => e instanceof NavigationStart)
    ).subscribe(() => {
      if (this.modal.visible()) this.modal.cancel();
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  @HostListener('document:keydown.escape')
  onEscape() {
    if (this.modal.visible()) this.modal.cancel();
  }
}
