import { Component, computed, inject, signal, TemplateRef, viewChild } from '@angular/core';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import {
  DropdownComponent,
  DropdownItem,
} from '../../../../shared/components/dropdown/dropdown.component';

import { AuthorListResponseDto } from '../../dtos/author-list-response.dto';
import { AuthorService } from '../../services/author.service';
import { ToastService } from '../../../../shared/services/toast';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { ModalService } from '../../../../shared/services/modal';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';

@Component({
  selector: 'app-author-list',
  standalone: true,
  imports: [
    EmptyStateComponent,
    DropdownComponent,
    SectionComponent,
    ButtonComponent,
    RouterLink,
    FormsModule,
    ReactiveFormsModule,
    InputComponent,
    PaginationComponent,
  ],
  templateUrl: './author-list.page.html',
  styleUrl: './author-list.page.css',
})
export class AuthorListPage {
  private readonly authorService = inject(AuthorService);
  private readonly toast = inject(ToastService);
  private readonly modal = inject(ModalService);
  private readonly fb = inject(FormBuilder);

  readonly authors = signal<AuthorListResponseDto[]>([]);
  readonly loading = signal(true);
  readonly deleting = signal(false);
  readonly updating = signal(false);
  readonly page = signal(1);
  readonly pageSize = signal(5);
  readonly totalElements = computed(() => this.authors().length);
  readonly totalPages = computed(() => {
    const size = this.pageSize();
    if (size === 0) return 0;
    return Math.ceil(this.totalElements() / size);
  });

  readonly paginated = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.authors().slice(start, start + this.pageSize());
  });

  authorToDelete: AuthorListResponseDto | null = null;
  authorToUpdate: AuthorListResponseDto | null = null;

  readonly deleteModalTplt = viewChild.required<TemplateRef<unknown>>('deleteModalTplt');
  readonly updateModalTplt = viewChild.required<TemplateRef<unknown>>('updateModalTplt');
  readonly deleteForm = this.fb.nonNullable.group({
    confirmation: ['', Validators.required],
  });

  readonly updateForm = this.fb.nonNullable.group({
    id: [{ value: '', disabled: true }],
    firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
    lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
  });

  constructor() {
    this.loadAuthors();
  }

  private loadAuthors(): void {
    this.loading.set(true);

    this.authorService.findAll().subscribe({
      next: (res) => {
        this.authors.set(res.data ?? []);
        this.loading.set(false);
        this.page.set(1);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger les auteurs.');
      },
    });
  }

  actionsFor(author: AuthorListResponseDto): DropdownItem[] {
    return [
      {
        label: 'Voir',
        icon: 'heroEye',
        action: () => this.toast.info(`Voir ${author.firstName} ${author.lastName}`),
      },
      {
        label: 'Modifier',
        icon: 'heroPencil',
        action: () => this.openUpdateModal(author),
      },
      {
        label: 'Supprimer',
        icon: 'heroTrash',
        danger: true,
        action: () => this.openDeleteModal(author),
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
    const fullName = this.authorToDelete
      ? `${this.authorToDelete.firstName} ${this.authorToDelete.lastName}`
      : '';

    return this.deleteForm.getRawValue().confirmation === fullName;
  }

  private openDeleteModal(author: AuthorListResponseDto): void {
    this.authorToDelete = author;
    this.deleteForm.reset();

    this.modal.open({
      title: `Supprimer ${author.firstName} ${author.lastName}`,
      template: this.deleteModalTplt(),
      size: 'sm',
    });
  }

  confirmDelete(): void {
    if (!this.isConfirmationValid() || !this.authorToDelete) {
      return;
    }

    this.deleting.set(true);

    const author = this.authorToDelete;

    this.authorService.delete(author.id.toString()).subscribe({
      next: (res) => {
        this.deleting.set(false);

        this.authors.update((list) => list.filter((a) => a.id !== author.id));

        if (this.paginated().length === 0 && this.page() > 1) {
          this.page.update((page) => page - 1);
        }

        this.toast.success(res.message);
        this.modal.close();
        this.authorToDelete = null;
      },
      error: (err) => {
        this.deleting.set(false);
        const message = err?.error?.message ?? "Impossible de supprimer l'auteur.";
        this.toast.danger(message);
      },
    });
  }

  private openUpdateModal(author: AuthorListResponseDto): void {
    this.authorToUpdate = author;

    this.updateForm.patchValue({
      id: author.id.toString(),
      firstName: author.firstName,
      lastName: author.lastName,
    });

    this.modal.open({
      title: `Modifier ${author.firstName} ${author.lastName}`,
      template: this.updateModalTplt(),
      size: 'sm',
    });
  }

  update(): void {
    if (this.updateForm.invalid || !this.authorToUpdate) {
      return;
    }

    this.updating.set(true);

    const author = this.authorToUpdate;
    const formValue = this.updateForm.getRawValue();

    const payload = {
      firstName: formValue.firstName,
      lastName: formValue.lastName,
    };

    this.authorService.update(Number(author.id), payload).subscribe({
      next: (res) => {
        this.updating.set(false);

        this.authors.update((list) =>
          list.map((a) => (a.id === author.id ? { ...a, ...payload } : a)),
        );

        this.toast.success(res.message);
        this.modal.close();
        this.authorToUpdate = null;
      },
      error: (err) => {
        this.updating.set(false);
        const message = err?.error?.message ?? 'Modification impossible.';
        this.toast.danger(message);
      },
    });
  }
}
