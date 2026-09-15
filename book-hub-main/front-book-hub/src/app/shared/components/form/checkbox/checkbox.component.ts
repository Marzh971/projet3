import {Component, computed, input, output} from '@angular/core';
import {cn} from '../../../../../utils/cn';

@Component({
  selector: 'bh-checkbox',
  standalone: true,
  templateUrl: './checkbox.component.html',
  styleUrl: './checkbox.component.css'
})
export class CheckboxComponent {
  label    = input<string>('');
  checked  = input<boolean>(false);
  disabled = input<boolean>(false);
  error    = input<string>('');

  changed = output<boolean>();

  id = `checkbox-${Math.random().toString(36).slice(2, 7)}`

  classes = computed(() => cn(
    'checkbox',
    this.checked()  && 'checkbox-checked',
    this.disabled() && 'checkbox-disabled',
    this.error()    && 'checkbox-error',
  ));
}
