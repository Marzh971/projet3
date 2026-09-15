import { Component, input } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { heroInbox, heroMagnifyingGlass, heroExclamationCircle } from '@ng-icons/heroicons/outline';

@Component({
  selector: 'bh-empty-state',
  standalone: true,
  imports: [NgIcon],
  providers: [provideIcons({ heroInbox, heroMagnifyingGlass, heroExclamationCircle })],
  templateUrl: './empty-state.component.html',
  styleUrl: './empty-state.component.css'
})
export class EmptyStateComponent {
  title   = input<string>('Aucun résultat');
  message = input<string>('');
  icon    = input<string>('heroInbox');
}
