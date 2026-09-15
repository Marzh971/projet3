import {Component, computed, input} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgTemplateOutlet} from '@angular/common';
import {cn} from '../../../../utils/cn';
import {BadgeComponent} from '../badge/badge.component';

@Component({
  selector: 'bh-card',
  standalone: true,
  imports: [RouterLink, NgTemplateOutlet, BadgeComponent],
  templateUrl: './card.component.html',
  styleUrl: './card.component.css',
})
export class CardComponent {
  // Apparence
  variant = input<'default' | 'raised'>('default');
  constrained = input<boolean>(false);
  interactive = input<boolean>(false);

  // Contenu
  title = input<string>('');
  description = input<string>();
  image       = input<string | null>();
  badge = input<string>();
  footer = input<string>();

  // Navigation
  route       = input<string | null>();
  fragment = input<string | undefined>(undefined);

  // Computed
  classes = computed(() => cn({
    'card': this.variant() === 'default',
    'card-raised': this.variant() === 'raised',
    'card-constrained': this.constrained(),
    'card-interactive': this.route() || this.interactive(),
  }));
}
