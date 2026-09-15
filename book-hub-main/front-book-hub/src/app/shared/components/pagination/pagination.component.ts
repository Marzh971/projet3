import {Component, computed, input, output} from '@angular/core';
import {cn} from '../../../../utils/cn';

@Component({
  selector: 'bh-pagination',
  standalone: true,
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {
  total = input.required<number>();
  page = input<number>(1);
  pageSize = input<number>(5);

  pageChanged = output<number>();
  pageSizeChanged = output<number>();

  pageSizes = [5, 10, 25];

  totalPages = computed(() => Math.ceil(this.total() / this.pageSize()));

  pages = computed(() => {
    const total = this.totalPages();
    if (total === 0) return [];
    return Array.from({ length: total }, (_, i) => i + 1);
  });

  cn = cn;
}
