import {
  Component,
  input,
  output,
  computed,
  forwardRef,
  signal,
  HostListener,
  ElementRef,
  inject,
} from '@angular/core';
import { cn } from '../../../../../utils/cn';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

export interface SelectOption {
  value: string | number;
  label: string;
  disabled?: boolean;
}

@Component({
  selector: 'bh-multi-select',
  standalone: true,
  templateUrl: './multi-select.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MultiSelectComponent),
      multi: true,
    },
  ],
  styleUrls: ['./multi-select.component.css'],
})
export class MultiSelectComponent implements ControlValueAccessor {
  private readonly elementRef = inject(ElementRef);
  options = input.required<SelectOption[]>();
  label = input<string>('');
  placeholder = input<string>('Rechercher...');
  error = input<string>('');
  disabled = input<boolean>(false);

  changed = output<(string | number)[]>();

  value = signal<(string | number)[]>([]);
  searchTerm = signal<string>('');
  isOpen = signal<boolean>(false);

  onChange: (val: any) => void = () => {};
  onTouched: () => void = () => {};

  filteredOptions = computed(() => {
    const term = this.searchTerm().toLowerCase();
    const selected = this.value();
    return this.options().filter(
      (opt) => !selected.includes(opt.value) && opt.label.toLowerCase().includes(term),
    );
  });

  selectedOptions = computed(() =>
    this.options().filter((opt) => this.value().includes(opt.value)),
  );

  containerClasses = computed(() =>
    cn(
      'input-field',
      'multi-select-field',
      'flex flex-wrap items-center gap-1 cursor-text',
      this.error() && 'input-error',
      this.disabled() && 'opacity-50 cursor-not-allowed',
    ),
  );

  writeValue(val: any): void {
    this.value.set(Array.isArray(val) ? val : []);
  }

  registerOnChange(fn: (val: any) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    // disabled géré via input() ci-dessus si besoin de forcer depuis le form
  }

  openDropdown(): void {
    if (!this.disabled()) {
      this.isOpen.set(true);
    }
  }

  closeDropdown(): void {
    this.isOpen.set(false);
    this.searchTerm.set('');
  }

  onSearchChange(term: string): void {
    this.searchTerm.set(term);
    this.isOpen.set(true);
  }

  addOption(optValue: string | number): void {
    const updated = [...this.value(), optValue];
    this.value.set(updated);
    this.onChange(updated);
    this.onTouched();
    this.changed.emit(updated);
    this.searchTerm.set('');
  }

  removeOption(optValue: string | number): void {
    const updated = this.value().filter((v) => v !== optValue);
    this.value.set(updated);
    this.onChange(updated);
    this.onTouched();
    this.changed.emit(updated);
  }

  // Retire le dernier tag si on appuie sur Backspace alors que l'input est vide
  onBackspace(): void {
    if (this.searchTerm() === '' && this.value().length > 0) {
      const last = this.value()[this.value().length - 1];
      this.removeOption(last);
    }
  }

}
