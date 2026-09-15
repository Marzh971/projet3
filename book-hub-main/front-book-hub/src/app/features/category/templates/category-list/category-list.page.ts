import { Component, computed, inject, signal, TemplateRef, viewChild } from '@angular/core';

import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { CategoryService } from '../../services/category-service';
import { ToastService } from '../../../../shared/services/toast';
import { CategoryResponseDTO } from '../../dtos/category-response.dto';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import {
  DropdownComponent,
  DropdownItem,
} from '../../../../shared/components/dropdown/dropdown.component';

import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { ModalService } from '../../../../shared/services/modal';
import { RouterLink } from '@angular/router';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';

@Component({
  selector: 'app-category-list',

  imports: [
    EmptyStateComponent,
    SectionComponent,
    FormsModule,
    ReactiveFormsModule,
    DropdownComponent,
    InputComponent,
    ButtonComponent,
    RouterLink,
    PaginationComponent,
  ],

  templateUrl: './category-list.page.html',
  styleUrl: './category-list.page.css',
})
export class CategoryListPage {
  private readonly categoryService = inject(CategoryService);
  private readonly toast = inject(ToastService);
  private readonly modal = inject(ModalService);
  private readonly fb = inject(FormBuilder);

  readonly categories = signal<CategoryResponseDTO[]>([]);
  readonly loading = signal(true);
  readonly deleting = signal(false);
  readonly updating = signal(false);
  readonly page = signal(1);
  readonly pageSize = signal(5);
  readonly totalElements = computed(() => this.categories().length);
  readonly totalPages = computed(() => {
    const size = this.pageSize();
    if (size === 0) return 0;
    return Math.ceil(this.totalElements() / size);
  });

  readonly paginated = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.categories().slice(start, start + this.pageSize());
  });

  categoryToDelete: CategoryResponseDTO | null = null;
  categoryToUpdate: CategoryResponseDTO | null = null;

  readonly deleteModalTplt = viewChild.required<TemplateRef<unknown>>('deleteModalTplt');
  readonly updateModalTplt = viewChild.required<TemplateRef<unknown>>('updateModalTplt');
  readonly deleteForm = this.fb.nonNullable.group({
    confirmation: ['', Validators.required],
  });

  readonly updateForm = this.fb.nonNullable.group({
    id: [
      {
        value: '',
        disabled: true,
      },
    ],
    nameCategory: ['', Validators.required],
  });

  constructor() {
    this.loadCategories();
  }

  private loadCategories(): void {
    this.loading.set(true);
    this.categoryService.findAll().subscribe({
      next: (res) => {
        this.categories.set(res.data ?? []);
        this.loading.set(false);
        this.page.set(1);
      },

      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger les catégories.');
      },
    });
  }

  actionsFor(category: CategoryResponseDTO): DropdownItem[] {
    return [
      {
        label: 'Modifier',
        icon: 'heroPencil',
        action: () => {
          this.openUpdateModal(category);
        },
      },

      {
        label: 'Supprimer',
        icon: 'heroTrash',
        danger: true,
        action: () => {
          this.openDeleteModal(category);
        },
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
    return this.deleteForm.getRawValue().confirmation === this.categoryToDelete?.nameCategory;
  }

  private openDeleteModal(category: CategoryResponseDTO): void {
    this.categoryToDelete = category;
    this.deleteForm.reset();
    this.modal.open({
      title: `Supprimer ${category.nameCategory}`,
      template: this.deleteModalTplt(),
      size: 'sm',
    });
  }

  confirmDelete(): void {
    if (!this.isConfirmationValid() || !this.categoryToDelete) {
      return;
    }

    this.deleting.set(true);
    const category = this.categoryToDelete;
    this.categoryService.delete(category.id).subscribe({
      next: (res) => {
        this.deleting.set(false);
        this.categories.update((list) => list.filter((c) => c.id !== category.id));

        if (this.paginated().length === 0 && this.page() > 1) {
          this.page.update((page) => page - 1);
        }

        this.toast.success(res.message);
        this.modal.close();
        this.categoryToDelete = null;
      },

      error: () => {
        this.deleting.set(false);
        this.toast.danger('Suppression impossible.');
      },
    });
  }

  private openUpdateModal(category: CategoryResponseDTO): void {
    this.categoryToUpdate = category;
    this.updateForm.patchValue({
      id: category.id.toString(),
      nameCategory: category.nameCategory,
    });

    this.modal.open({
      title: `Modifier ${category.nameCategory}`,
      template: this.updateModalTplt(),
      size: 'sm',
    });
  }

  update(): void {
    if (this.updateForm.invalid || !this.categoryToUpdate) {
      return;
    }

    this.updating.set(true);
    const category = this.categoryToUpdate;
    const formValue = this.updateForm.getRawValue();
    const payload: CategoryResponseDTO = {
      id: category.id,
      nameCategory: formValue.nameCategory,
    };

    this.categoryService.update(category.id, payload).subscribe({
      next: (res) => {
        this.updating.set(false);
        this.categories.update((list) =>
          list.map((c) => (c.id === category.id ? (res.data ?? payload) : c)),
        );

        this.toast.success(res.message);
        this.modal.close();
        this.categoryToUpdate = null;
      },

      error: () => {
        this.updating.set(false);
        this.toast.danger('Modification impossible.');
      },
    });
  }
}
