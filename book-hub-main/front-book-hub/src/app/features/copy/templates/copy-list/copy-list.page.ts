import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CopyListResponseDto } from '../../dtos/copy-list-response.dto';
import { ToastService } from '../../../../shared/services/toast';
import { CopyService } from '../../services/copy.service';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { CardComponent } from '../../../../shared/components/card/card.component';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';

@Component({
  selector: 'app-copy-list',
  standalone: true,
  templateUrl: './copy-list.page.html',
  imports: [SectionComponent, EmptyStateComponent, CardComponent, PaginationComponent],
})
export class CopyListPage implements OnInit {
  private readonly copyService = inject(CopyService);
  private readonly toast = inject(ToastService);

  // Contient l'intégralité des exemplaires renvoyés par le Back-end
  // (le Back-end ne supporte pas encore la pagination pour cet endpoint)
  private readonly allCopies = signal<CopyListResponseDto[]>([]);

  readonly loading = signal(true);

  // Pagination (gérée côté front en attendant un support serveur)
  page = signal(0);
  pageSize = signal(5);

  readonly totalElements = computed(() => this.allCopies().length);

  readonly totalPages = computed(() => {
    const size = this.pageSize();
    if (size === 0) return 0;
    return Math.ceil(this.totalElements() / size);
  });

  // Exemplaires affichés pour la page courante
  readonly copies = computed(() => {
    const start = this.page() * this.pageSize();
    const end = start + this.pageSize();
    return this.allCopies().slice(start, end);
  });

  ngOnInit(): void {
    this.loadCopies();
  }

  private loadCopies(): void {
    this.loading.set(true);

    this.copyService.findAll().subscribe({
      next: (res) => {
        this.allCopies.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger les copies.');
      },
    });
  }

  columns(): number {
    if (window.innerWidth < 480) return 1;
    if (window.innerWidth < 768) return 2;
    if (window.innerWidth < 1200) return 3;
    return 5;
  }

  protected onPageChange(newPageHuman: number): void {
    this.page.set(newPageHuman - 1);
  }

  protected onPageSizeChange(newSize: number): void {
    this.pageSize.set(newSize);
    this.page.set(0);
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleDateString('fr-FR');
  }
}
