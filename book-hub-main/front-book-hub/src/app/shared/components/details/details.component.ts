import { Component, input } from '@angular/core';
import { DetailsItem } from './details.model';

@Component({
  selector: 'bh-details',
  templateUrl: './details.component.html',
  styleUrl: './details.component.css',
})
export class DetailsComponent {
  readonly details = input<DetailsItem[]>([]);
}
