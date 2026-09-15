import { Component, computed, inject, signal } from '@angular/core';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import {
  DropdownComponent,
  DropdownItem,
} from '../../../../shared/components/dropdown/dropdown.component';

import { LoanListResponseDto } from '../../dtos/loan-list-response.dto';
import { LoanService } from '../../services/loan.service';
import { ToastService } from '../../../../shared/services/toast';
import { DatePipe, formatDate } from '@angular/common';
import { SectionComponent } from '../../../../shared/components/section/section.component';


@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [
    EmptyStateComponent,
    DropdownComponent,
    SectionComponent,
    DatePipe,
  ],
  templateUrl: './loan-list.page.html',
})
export class LoanListPage {
  private readonly loanService = inject(LoanService);
  private readonly toast = inject(ToastService);

  readonly loans = signal<LoanListResponseDto[]>([]);
  readonly loading = signal(true);

  page = signal(1);
  pageSize = signal(5);

  readonly total = computed(() => this.loans().length);

  readonly paginated = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.loans().slice(start, start + this.pageSize());
  });

  constructor() {
    this.loadLoans();
  }

  private loadLoans(): void {
    this.loading.set(true);

    this.loanService.findAll().subscribe({
      next: (res) => {
        console.log(res)
        this.loans.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger les emprunts.');
      },
    });
  }

  actionsFor(loan: LoanListResponseDto): DropdownItem[] {
    return [
      {
        label: 'Voir',
        icon: 'heroEye',
        action: () => this.toast.info(`Voir l'emprunt #${loan.id}`),
      },
      {
        label: 'Modifier',
        icon: 'heroPencil',
        badge: {
          text: 'bientôt',
          variant: 'neutral',
        },
        action: () => this.toast.info(`Modifier l'emprunt #${loan.id}`),
        disabled: true,
      },
      {
        label: 'Supprimer',
        icon: 'heroTrash',
        danger: true,
        action: () => this.deleteLoan(loan),
      },
    ];
  }

  private deleteLoan(loan: LoanListResponseDto): void {
    this.loanService.delete(loan.id.toString()).subscribe({
      next: (res) => {
        this.loans.update((list) => list.filter((l) => l.id !== loan.id));

        this.toast.success(res.message);
      },
      error: () => {
        this.toast.danger("Impossible de supprimer l'emprunt.");
      },
    });
  }

  protected readonly formatDate = formatDate;
}
