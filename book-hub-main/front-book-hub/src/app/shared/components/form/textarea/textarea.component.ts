import { Component, input, output, computed } from '@angular/core';
import {cn} from '../../../../../utils/cn';


@Component({
  selector: 'bh-textarea',
  standalone: true,
  templateUrl: './textarea.component.html',
  styleUrl: './textarea.component.css'
})
export class TextareaComponent {
  label       = input<string>('');
  placeholder = input<string>('');
  value       = input<string>('');
  error       = input<string>('');
  disabled    = input<boolean>(false);
  rows        = input<number>(4);

  changed = output<string>();

  classes = computed(() => cn(
    'input-field',
    'textarea-field',
    this.error()    && 'input-error',
    this.disabled() && 'opacity-50 cursor-not-allowed'
  ));
}
