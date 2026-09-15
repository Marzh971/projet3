import {Component, computed, input} from '@angular/core';
import {cn} from '../../../../utils/cn';


@Component({
  selector: 'bh-avatar',
  standalone: true,
  templateUrl: './avatar.component.html',
  styleUrl: './avatar.component.css',
})
export class AvatarComponent {
  src = input<string>('');
  name = input<string>('');
  size = input<'sm' | 'md' | 'lg' | 'xl'>('md');
  status = input<'online' | 'offline' | 'away' | null>(null);

  initials = computed(() => {
    const n = this.name().trim();
    if (!n) return '?';
    const parts = n.split(' ');
    return parts.length >= 2
      ? (parts[0][0] + parts[parts.length - 1][0]).toUpperCase()
      : n.slice(0, 2).toUpperCase();
  });

  classes = computed(() => cn(
    'avatar',
    `avatar-${this.size()}`,
  ));

  hasError = false;

  onImgError() {
    this.hasError = true;
  }
}
