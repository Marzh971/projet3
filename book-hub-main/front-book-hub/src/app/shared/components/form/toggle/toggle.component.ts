import { Component, input, output, computed } from '@angular/core';
import {cn} from '../../../../../utils/cn';

@Component({
  selector: 'bh-toggle',
  standalone: true,
  templateUrl: './toggle.component.html',
  styleUrl: './toggle.component.css'
})
export class ToggleComponent {
  label    = input<string>('');
  checked  = input<boolean>(false);
  disabled = input<boolean>(false);
  size     = input<'sm' | 'md'>('md');

  changed = output<boolean>();

  trackClasses = computed(() => cn(
    'toggle-track',
    `toggle-track-${this.size()}`,
    this.checked()  && 'toggle-track-checked',
    this.disabled() && 'toggle-track-disabled',
  ));

  thumbClasses = computed(() => cn(
    'toggle-thumb',
    `toggle-thumb-${this.size()}`,
    this.checked() && `toggle-thumb-checked-${this.size()}`,
  ));
}
