import {Component, computed, Input, input, InputSignal} from '@angular/core';
import {cn} from '../../../../utils/cn';

@Component({
  selector: 'bh-badge',
  standalone: true,
  templateUrl: './badge.component.html',
  styleUrl: './badge.component.css',
})
export class BadgeComponent {
  variant = input<'primary' | 'danger' | 'neutral'>('neutral');

  classes = computed(() => cn(
    'badge',
    {
      'badge-primary': this.variant() === 'primary',
      'badge-danger': this.variant() === 'danger',
      'badge-neutral': this.variant() === 'neutral',
    }
  ))
}
