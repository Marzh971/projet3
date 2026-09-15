import { Component, input } from '@angular/core';

@Component({
  selector: 'bh-divider',
  standalone: true,
  host: {
    '[style.display]': `orientation() === 'vertical' ? 'flex' : 'contents'`,
    '[style.align-self]': `orientation() === 'vertical' ? 'stretch' : null`,
  },
  templateUrl: './divider.component.html',
  styleUrl: './divider.component.css'
})
export class DividerComponent {
  label       = input<string>('');
  orientation = input<'horizontal' | 'vertical'>('horizontal');
}
