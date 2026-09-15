import { EmptyStateComponent } from '../../../shared/components/empty-state/empty-state.component';
import { SectionComponent } from '../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import {
  DropdownComponent,
  DropdownItem,
} from '../../../shared/components/dropdown/dropdown.component';
import { InputComponent } from '../../../shared/components/form/input/input.component';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReviewService } from '../services/review.service';
import { Component, computed, inject, signal, TemplateRef, viewChild } from '@angular/core';
import { ToastService } from '../../../shared/services/toast';
import { ModalService } from '../../../shared/services/modal';
import { ReviewListResponseDto } from '../dtos/review-list-response.dto';
import { ReviewUpdateRequestDto } from '../dtos/review-update.dto';


@Component({
  selector: 'app-review-list',
  standalone: true,
  imports: [
    EmptyStateComponent,
    SectionComponent,
    ButtonComponent,
    DropdownComponent,
    InputComponent,
    PaginationComponent,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './review-list.page.html',
  styleUrl: './review-list.page.css',
})
export class ReviewListPage {
  private readonly reviewService = inject(ReviewService);
  private readonly toast = inject(ToastService);
  private readonly modal = inject(ModalService);
  private readonly fb = inject(FormBuilder);

  readonly reviews = signal<ReviewListResponseDto[]>([]);

  readonly loading = signal(true);

  readonly deleting = signal(false);

  readonly updating = signal(false);

  readonly page = signal(1);

  readonly pageSize = signal(5);

  readonly totalElements = computed(() => this.reviews().length);

  readonly totalPages = computed(() => {
    const size = this.pageSize();
    if (size === 0) return 0;
    return Math.ceil(this.totalElements() / size);
  });

  readonly paginated = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.reviews().slice(start, start + this.pageSize());
  });

  reviewToDelete: ReviewListResponseDto | null = null;

  reviewToUpdate: ReviewListResponseDto | null = null;

  readonly deleteModalTplt = viewChild.required<TemplateRef<unknown>>('deleteModalTplt');

  readonly updateModalTplt = viewChild.required<TemplateRef<unknown>>('updateModalTplt');

  readonly updateForm = this.fb.nonNullable.group({
    rate: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    comment: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(500)]],
  });

  constructor() {
    this.loadReviews();
  }

  private loadReviews(): void {
    this.loading.set(true);

    this.reviewService.findAll().subscribe({
      next: (res) => {
        this.reviews.set(res.data ?? []);
        this.loading.set(false);
        this.page.set(1);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger les avis.');
      },
    });
  }

  actionsFor(review: ReviewListResponseDto): DropdownItem[] {
    return [
      {
        label: 'Voir',
        icon: 'heroEye',
        action: () => this.toast.info(`Avis noté ${review.rate}/5`),
      },
      {
        label: 'Modifier',
        icon: 'heroPencil',
        action: () => this.openUpdateModal(review),
      },
      {
        label: 'Supprimer',
        icon: 'heroTrash',
        danger: true,
        action: () => this.openDeleteModal(review),
      },
    ];
  }

  onPageChange(newPage: number): void {
    this.page.set(newPage);
  }

  onPageSizeChange(newSize: number): void {
    this.pageSize.set(newSize);
    this.page.set(1);
  }

  private openDeleteModal(review: ReviewListResponseDto): void {
    this.reviewToDelete = review;

    this.modal.open({
      title: 'Supprimer cet avis',
      template: this.deleteModalTplt(),
      size: 'sm',
    });
  }

  cancelDelete(): void {
    this.reviewToDelete = null;
    this.modal.close();
  }

  confirmDelete(): void {
    if (!this.reviewToDelete) {
      return;
    }

    this.deleting.set(true);

    const review = this.reviewToDelete;

    this.reviewService.delete(review.id.toString()).subscribe({
      next: (res) => {
        this.deleting.set(false);

        this.reviews.update((list) => list.filter((r) => r.id !== review.id));

        if (this.paginated().length === 0 && this.page() > 1) {
          this.page.update((page) => page - 1);
        }

        this.toast.success(res.message);
        this.modal.close();
        this.reviewToDelete = null;
      },
      error: (err) => {
        this.deleting.set(false);
        const message = err?.error?.message ?? "Impossible de supprimer l'avis.";
        this.toast.danger(message);
      },
    });
  }

  private openUpdateModal(review: ReviewListResponseDto): void {
    this.reviewToUpdate = review;

    this.updateForm.patchValue({
      rate: review.rate,
      comment: review.comment,
    });

    this.modal.open({
      title: 'Modifier un avis',
      template: this.updateModalTplt(),
      size: 'sm',
    });
  }

  update(): void {
    if (this.updateForm.invalid || !this.reviewToUpdate) {
      return;
    }

    this.updating.set(true);

    const review = this.reviewToUpdate;
    const formValue = this.updateForm.getRawValue();

    const payload: ReviewUpdateRequestDto = {
      rate: formValue.rate,
      comment: formValue.comment,
    };

    this.reviewService.update(review.id.toString(), payload).subscribe({
      next: (res) => {
        this.updating.set(false);

        this.reviews.update((list) =>
          list.map((r) => (r.id === review.id ? { ...r, ...payload } : r)),
        );

        this.toast.success(res.message);
        this.modal.close();
        this.reviewToUpdate = null;
      },
      error: (err) => {
        this.updating.set(false);
        const message = err?.error?.message ?? 'Modification impossible.';
        this.toast.danger(message);
      },
    });
  }
}
