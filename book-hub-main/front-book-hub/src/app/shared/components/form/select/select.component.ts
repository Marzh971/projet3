import { Component, input, output, computed, forwardRef, signal } from '@angular/core';
import { cn } from '../../../../../utils/cn';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

export interface SelectOption {
  value: string | number;
  label: string;
  disabled?: boolean;
}

@Component({
  selector: 'bh-select',
  standalone: true,
  templateUrl: './select.component.html',
  styleUrl: './select.component.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true,
    },
  ],
})
export class SelectComponent implements ControlValueAccessor {
  options = input.required<SelectOption[]>();
  label = input<string>('');
  placeholder = input<string>('Choisir...');
  error = input<string>('');
  disabled = input<boolean>(false);

  changed = output<string | number>();

  value = signal<string | number>('');
  onChange: (val: any) => void = () => {};
  onTouched: () => void = () => {};

  writeValue(val: any): void {
    this.value.set(val ?? ''); // Corrigé ici
  }

  registerOnChange(fn: (val: any) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  onSelectionChange(event: Event): void {
    const val = (event.target as HTMLSelectElement).value;
    this.value.set(val);
    this.onChange(val);
    this.changed.emit(val);
  }

  classes = computed(() =>
    cn(
      'input-field',
      'select-field',
      this.error() && 'input-error',
      this.disabled() && 'opacity-50 cursor-not-allowed',
    ),
  );
}
