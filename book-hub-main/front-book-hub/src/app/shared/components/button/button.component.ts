import { Component, computed, input } from '@angular/core';
import { cn } from '../../../../utils/cn';

@Component({
  selector: 'bh-button',
  standalone: true,
  templateUrl: './button.component.html',
  styleUrl: './button.component.css',
})
export class ButtonComponent {
  variant = input<'primary' | 'ghost' | 'danger'>('primary');
  size = input<'sm' | 'md' | 'lg'>('md');
  disabled = input<boolean>(false);
  label = input<string>('');
  type = input<'button' | 'submit' | 'reset'>('button');

  classes = computed(() =>
    cn(
      'btn',
      {
        'btn-primary': this.variant() === 'primary',
        'btn-ghost': this.variant() === 'ghost',
        'btn-danger': this.variant() === 'danger',
      },
      {
        'text-xs px-3 py-1': this.size() === 'sm',
        'text-sm px-4 py-2': this.size() === 'md',
        'text-base px-6 py-3': this.size() === 'lg',
      },
      this.disabled() && 'opacity-50 cursor-not-allowed',
    ),
  );
}
