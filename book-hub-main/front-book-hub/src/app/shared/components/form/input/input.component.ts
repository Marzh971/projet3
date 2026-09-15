import {Component, computed, forwardRef, input} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {cn} from '../../../../../utils/cn';

@Component({
  selector: 'bh-input',
  standalone: true,
  templateUrl: './input.component.html',
  styleUrl: './input.component.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true,
    },
  ],
})
export class InputComponent implements ControlValueAccessor {
  label = input<string>('');
  placeholder = input<string>('');
  type = input<'text' | 'email' | 'password' | 'number' | 'date'>('text');
  error = input<string>('');
  disabled = input<boolean>(false);

  value = '';
  onChange = (_: string) => {};
  onTouched = () => {};

  writeValue(val: string) {
    this.value = val ?? '';
  }
  registerOnChange(fn: any) {
    this.onChange = fn;
  }
  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  classes = computed(() =>
    cn(
      'input-field',
      this.error() && 'input-error',
      this.disabled() && 'opacity-50 cursor-not-allowed',
    ),
  );
}
