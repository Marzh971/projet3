import { Component, computed, inject, OnInit, signal, TemplateRef, viewChild } from '@angular/core';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { UserListResponseDto } from '../../dtos/user-list-response.dto';
import { UserService } from '../../services/user.service';
import { ToastService } from '../../../../shared/services/toast';
import {
  DropdownComponent,
  DropdownItem,
} from '../../../../shared/components/dropdown/dropdown.component';
import { ModalService } from '../../../../shared/services/modal';
import { UserResponseDto } from '../../dtos/user-response.dto';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-list',
  imports: [
    EmptyStateComponent,
    DropdownComponent,
    ReactiveFormsModule,
    InputComponent,
    ButtonComponent,
    SectionComponent,
    RouterLink,
  ],
  templateUrl: './user-list.page.html',
})
export class UserListPage implements OnInit {
  private readonly userService = inject(UserService);
  private readonly toast = inject(ToastService);
  private readonly modal = inject(ModalService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  readonly users = signal<UserListResponseDto[]>([]);
  readonly loading = signal(true);
  readonly deleting = signal(false);

  page = signal(1);
  pageSize = signal(5);

  readonly total = computed(() => this.users().length);

  userToDelete: UserListResponseDto | null = null;

  readonly paginated = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.users().slice(start, start + this.pageSize());
  });

  readonly deleteModalTplt = viewChild.required<TemplateRef<unknown>>('deleteModalTplt');

  readonly deleteForm = this.fb.nonNullable.group({
    confirmation: ['', Validators.required],
  });

  ngOnInit(): void {
    this.loading.set(true);
    this.userService.findAll().subscribe({
      next: (res) => {
        this.users.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger les utilisateurs.');
      },
    });
  }

  isConfirmationValid(): boolean {
    return this.deleteForm.getRawValue().confirmation === this.userToDelete?.username;
  }

  actionsFor(user: UserListResponseDto): DropdownItem[] {
    return [
      {
        label: 'Voir',
        icon: 'heroEye',
        action: () => this.router.navigate([`/users/${user.id}`]),
      },
      {
        label: 'Modifier',
        icon: 'heroPencil',
        action: () => this.toast.info(`Modifier ${user.username}`),
      },
      {
        label: 'Supprimer',
        icon: 'heroTrash',
        danger: true,
        action: () => this.openDeleteModal(user),
      },
    ];
  }
  private openDeleteModal(user: UserListResponseDto): void {
    this.userToDelete = user;
    this.modal.open({
      title: `Supprimer ${user.username}`,
      template: this.deleteModalTplt(),
      size: 'sm',
    });
  }

  confirmDelete(): void {
    if (!this.isConfirmationValid() || !this.userToDelete) return;

    this.deleting.set(true);
    const user = this.userToDelete;

    this.userService.delete(user.id).subscribe({
      next: (res) => {
        this.deleting.set(false);
        this.users.update((list) => list.filter((u) => u.id !== user.id));
        this.toast.success(res.message);
        this.modal.close();
      },
      error: () => {
        this.deleting.set(false);
        this.toast.danger('Suppression impossible.');
      },
    });
  }
}
