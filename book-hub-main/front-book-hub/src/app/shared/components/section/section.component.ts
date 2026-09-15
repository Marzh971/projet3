import {Component, computed, input} from '@angular/core';
import {DividerComponent} from '../divider/divider.component';
import { BadgeComponent } from '../badge/badge.component';

@Component({
  selector: 'bh-section',
  standalone: true,
  templateUrl: './section.component.html',
  styleUrl: './section.component.css',
  imports: [DividerComponent, BadgeComponent]
})
export class SectionComponent {
  title       = input<string>('');
  description = input<string>('');
  sectionId   = input<string>('');
  columns    = input<number>(1);
  label       = input<string>('');
  labelColor = input<'primary' | 'danger' | 'neutral'>('neutral');
  titleSize   = input<'level-1' | 'level-2' | 'level-3'>('level-1');

  gridColumns = computed(() =>
    this.columns() > 1 ? `repeat(${this.columns()}, 1fr)` : '1fr'
  );
}
