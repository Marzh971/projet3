import { Component, input, output, computed } from '@angular/core';
import {cn} from '../../../../../utils/cn';

export interface RadioOption {
  value: string | number;
  label: string;
  description?: string;
  disabled?: boolean;
}

@Component({
  selector: 'bh-radio-group',
  standalone: true,
  templateUrl: './radio-group.component.html',
  styleUrl: './radio-group.component.css'
})
export class RadioGroupComponent {
  options  = input.required<RadioOption[]>();
  value    = input<string | number>('');
  label    = input<string>('');
  layout   = input<'vertical' | 'horizontal'>('vertical');
  disabled = input<boolean>(false);
  error    = input<string>('');

  changed = output<string | number>();

  groupId = `radio-${Math.random().toString(36).slice(2, 7)}`;

  containerClasses = computed(() => cn(
    'radio-group',
    this.layout() === 'horizontal' && 'radio-group-horizontal'
  ));
}
