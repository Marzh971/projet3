import { Component, input, signal, HostListener, ElementRef } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { heroPencil, heroTrash, heroEye, heroEllipsisVertical } from '@ng-icons/heroicons/outline';
import {BadgeComponent} from '../badge/badge.component';

export interface DropdownItem {
  label:    string;
  icon:     string;
  badge?:   { text: string; variant: 'primary' | 'danger' | 'neutral' };
  action:   () => void;
  disabled?: boolean;
  danger?:  boolean;
}

@Component({
  selector: 'bh-dropdown',
  standalone: true,
  imports: [NgIcon, BadgeComponent,],
  providers: [provideIcons({ heroPencil, heroTrash, heroEye, heroEllipsisVertical })],
  templateUrl: './dropdown.component.html',
  styleUrl: './dropdown.component.css'
})
export class DropdownComponent {
  items   = input.required<DropdownItem[]>();
  trigger = input<string>('heroEllipsisVertical');

  open     = signal(false);
  menuTop  = signal(0);
  menuLeft = signal(0);

  constructor(private el: ElementRef) {}

  toggle(event: MouseEvent) {
    event.stopPropagation();
    const rect = (event.currentTarget as HTMLElement).getBoundingClientRect();
    this.menuTop.set(rect.bottom + window.scrollY + 4);
    this.menuLeft.set(rect.right - 180);
    this.open.update(v => !v);
  }

  select(item: DropdownItem) {
    if (item.disabled) return;
    item.action();
    this.open.set(false);
  }

  @HostListener('document:click')
  onOutsideClick() { this.open.set(false); }
}
