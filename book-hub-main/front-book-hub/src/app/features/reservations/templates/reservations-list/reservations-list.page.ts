import { Component, computed, inject, signal, TemplateRef, viewChild } from '@angular/core';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { ReservationsService } from '../../services/reservations-service';
import { ToastService } from '../../../../shared/services/toast';
import { ReservationsResponseDto } from '../../dtos/reservations-response.dto';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { DropdownComponent, DropdownItem } from '../../../../shared/components/dropdown/dropdown.component';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { ModalService } from '../../../../shared/services/modal';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-reservations-list',
  imports: [
    EmptyStateComponent,
    SectionComponent,
    FormsModule,
    ReactiveFormsModule,
    DropdownComponent,
    InputComponent,
    ButtonComponent,
    DatePipe,
  ],
  templateUrl: './reservations-list.page.html',
})
export class ReservationsListPage {
  private readonly reservationsService = inject(ReservationsService);
  private readonly toast = inject(ToastService);
  private readonly modal = inject(ModalService);
  private readonly fb = inject(FormBuilder);

  readonly categories = signal<ReservationsResponseDto[]>([]);
  readonly loading = signal(true);
  readonly deleting = signal(false);
  readonly updating = signal(false);

  page = signal(1);
  pageSize = signal(5);

  readonly paginated = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.categories().slice(start, start + this.pageSize());
  });

  reservationToDelete: ReservationsResponseDto | null = null;
  reservationToUpdate: ReservationsResponseDto | null = null;

  readonly deleteModalTplt = viewChild.required<TemplateRef<unknown>>('deleteModalTplt');
  readonly updateModalTplt = viewChild.required<TemplateRef<unknown>>('updateModalTplt');

  readonly deleteForm = this.fb.nonNullable.group({
    confirmation: ['', Validators.required],
  });
  readonly updateForm = this.fb.nonNullable.group({
    id: [{ value: '', disabled: true }],
    nameCategory: ['', Validators.required],
  });

  constructor() {
    this.loadCategories();
  }

  private loadCategories(): void {
    this.loading.set(true);
    this.reservationsService.findAll().subscribe({
      next: (res) => {
        this.categories.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger les catégories.');
      },
    });
  }

  actionsFor(reservation: ReservationsResponseDto): DropdownItem[] {
    // @ts-ignore
    return [
      {
        label: 'Voir',
        icon: 'heroEye',
        action: () => this.toast.info(`Voir ${reservation.id}`),
      },
      // {
      //   label: 'Modifier',
      //   icon: 'heroPencil',
      //   action: () => this.openUpdateModal(category),
      // },
      {
        label: 'Supprimer',
        icon: 'heroTrash',
        danger: true,
        action: () => this.openDeleteModal(reservation),
      },
    ];
  }

  isConfirmationValid(): boolean {
    return this.deleteForm.getRawValue().confirmation === this.reservationToDelete?.id?.toString();
  }

  private openDeleteModal(reservation: ReservationsResponseDto): void {
    this.reservationToDelete = reservation;
    this.modal.open({
      title: `Supprimer ${reservation.id}`,
      template: this.deleteModalTplt(),
      size: 'sm',
    });
  }

  confirmDelete(): void {
    if (!this.isConfirmationValid() || !this.reservationToDelete) return;

    this.deleting.set(true);
    const reservation = this.reservationToDelete;

    this.reservationsService.delete(Number(reservation.id)).subscribe({
      next: (res) => {
        this.deleting.set(false);
        this.categories.update((list) => list.filter((u) => u.id !== reservation.id));
        this.toast.success(res.message);
        this.modal.close();
      },
      error: () => {
        this.deleting.set(false);
        this.toast.danger('Suppression impossible.');
      },
    });
  }

  // private openUpdateModal(reservation: ReservationsResponseDto): void {
  //   this.reservationToUpdate = reservation;
  //   this.updateForm.patchValue({
  //     id: reservation.id.toString(),
  //     nameCategory: category.nameCategory,
  //   });
  //   this.modal.open({
  //     title: `Modifier ${category.nameCategory}`,
  //     template: this.updateModalTplt(),
  //     size: 'sm',
  //   });
  // }
  //
  // update(): void {
  //   if (!this.isConfirmationValid() || !this.reservationToUpdate) return;
  //
  //   this.updating.set(true);
  //   const { namereservation } = this.updateForm.getRawValue();
  //   const reservation = this.categoryToUpdate;
  //   const payload: ReservationsResponseDto = { ...category, nameCategory };
  //
  //   this.categoryService.update(category.id, payload).subscribe({
  //     next: (res) => {
  //       this.updating.set(false);
  //       this.categories.update((list) =>
  //         list.map((c) => (c.id === category.id ? (res.data ?? payload) : c)),
  //       );
  //       this.toast.success(res.message);
  //       this.modal.close();
  //     },
  //     error: () => {
  //       this.updating.set(false);
  //       this.toast.danger('Modification impossible.');
  //     },
  //   });
  // }
}
