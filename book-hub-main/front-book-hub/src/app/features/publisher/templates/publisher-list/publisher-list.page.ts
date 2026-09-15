import { Component, computed, inject, signal, TemplateRef, viewChild } from '@angular/core';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import {
  DropdownComponent,
  DropdownItem,
} from '../../../../shared/components/dropdown/dropdown.component';

import { PublisherListResponseDto } from '../../dtos/publisher-list-response.dto';
import { PublisherService } from '../../services/publisher.service';
import { ToastService } from '../../../../shared/services/toast';
import { RouterLink } from '@angular/router';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { ModalService } from '../../../../shared/services/modal';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';

@Component({
  selector: 'app-publisher-list',
  standalone: true,
  imports: [
    EmptyStateComponent,
    DropdownComponent,
    RouterLink,
    ButtonComponent,
    SectionComponent,
    FormsModule,
    ReactiveFormsModule,
    InputComponent,
    PaginationComponent,
  ],
  templateUrl: './publisher-list.page.html',
  styleUrl: './publisher-list.page.css',
})
export class PublisherListPage {
  private readonly publisherService = inject(PublisherService);
  private readonly toast = inject(ToastService);
  private readonly modal = inject(ModalService);
  private readonly fb = inject(FormBuilder);

  readonly publishers = signal<PublisherListResponseDto[]>([]);

  readonly loading = signal(true);

  readonly deleting = signal(false);

  readonly updating = signal(false);

  readonly page = signal(1);

  readonly pageSize = signal(5);

  readonly totalElements = computed(() => this.publishers().length);

  readonly totalPages = computed(() => {
    const size = this.pageSize();
    if (size === 0) return 0;
    return Math.ceil(this.totalElements() / size);
  });

  readonly paginated = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.publishers().slice(start, start + this.pageSize());
  });

  publisherToDelete: PublisherListResponseDto | null = null;

  publisherToUpdate: PublisherListResponseDto | null = null;

  readonly deleteModalTplt = viewChild.required<TemplateRef<unknown>>('deleteModalTplt');

  readonly updateModalTplt = viewChild.required<TemplateRef<unknown>>('updateModalTplt');

  readonly deleteForm = this.fb.nonNullable.group({
    confirmation: ['', Validators.required],
  });

  readonly updateForm = this.fb.nonNullable.group({
    id: [{ value: '', disabled: true }],
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
  });

  constructor() {
    this.loadPublishers();
  }

  private loadPublishers(): void {
    this.loading.set(true);

    this.publisherService.findAll().subscribe({
      next: (res) => {
        this.publishers.set(res.data ?? []);
        this.loading.set(false);
        this.page.set(1);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger les éditeurs.');
      },
    });
  }

  actionsFor(publisher: PublisherListResponseDto): DropdownItem[] {
    return [
      {
        label: 'Voir',
        icon: 'heroEye',
        action: () => this.toast.info(`Voir ${publisher.name}`),
      },
      {
        label: 'Modifier',
        icon: 'heroPencil',
        action: () => this.openUpdateModal(publisher),
      },
      {
        label: 'Supprimer',
        icon: 'heroTrash',
        danger: true,
        action: () => this.openDeleteModal(publisher),
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

  isConfirmationValid(): boolean {
    return this.deleteForm.getRawValue().confirmation === this.publisherToDelete?.name;
  }

  private openDeleteModal(publisher: PublisherListResponseDto): void {
    this.publisherToDelete = publisher;
    this.deleteForm.reset();

    this.modal.open({
      title: `Supprimer ${publisher.name}`,
      template: this.deleteModalTplt(),
      size: 'sm',
    });
  }

  confirmDelete(): void {
    if (!this.isConfirmationValid() || !this.publisherToDelete) {
      return;
    }

    this.deleting.set(true);

    const publisher = this.publisherToDelete;

    this.publisherService.delete(publisher.id.toString()).subscribe({
      next: (res) => {
        this.deleting.set(false);

        this.publishers.update((list) => list.filter((p) => p.id !== publisher.id));

        if (this.paginated().length === 0 && this.page() > 1) {
          this.page.update((page) => page - 1);
        }

        this.toast.success(res.message);
        this.modal.close();
        this.publisherToDelete = null;
      },
      error: (err) => {
        this.deleting.set(false);
        const message = err?.error?.message ?? "Impossible de supprimer l'éditeur.";
        this.toast.danger(message);
      },
    });
  }

  private openUpdateModal(publisher: PublisherListResponseDto): void {
    this.publisherToUpdate = publisher;

    this.updateForm.patchValue({
      id: publisher.id.toString(),
      name: publisher.name,
    });

    this.modal.open({
      title: `Modifier ${publisher.name}`,
      template: this.updateModalTplt(),
      size: 'sm',
    });
  }

  update(): void {
    if (this.updateForm.invalid || !this.publisherToUpdate) {
      return;
    }

    this.updating.set(true);

    const publisher = this.publisherToUpdate;
    const formValue = this.updateForm.getRawValue();

    const payload = {
      name: formValue.name,
    };

    this.publisherService.update(publisher.id.toString(), payload).subscribe({
      next: (res) => {
        this.updating.set(false);

        this.publishers.update((list) =>
          list.map((p) => (p.id === publisher.id ? { ...p, ...payload } : p)),
        );

        this.toast.success(res.message);
        this.modal.close();
        this.publisherToUpdate = null;
      },
      error: (err) => {
        this.updating.set(false);
        const message = err?.error?.message ?? 'Modification impossible.';
        this.toast.danger(message);
      },
    });
  }
}
