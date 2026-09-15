import { Component, inject, OnInit, signal } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserResponseDto } from '../../dtos/user-response.dto';
import { ActivatedRoute } from '@angular/router';
import { ToastService } from '../../../../shared/services/toast';
import { AuthService } from '../../../auth/services/auth.service';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { DetailsComponent } from '../../../../shared/components/details/details.component';
import { DetailsItem } from '../../../../shared/components/details/details.model';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  templateUrl: './user-detail.page.html',
  imports: [SectionComponent, EmptyStateComponent, DetailsComponent],
})
export class UserDetailPage implements OnInit {
  private readonly userService = inject(UserService);
  private readonly route = inject(ActivatedRoute);
  private readonly toast = inject(ToastService);
  private readonly authService = inject(AuthService);

  readonly user = signal<UserResponseDto | null>(null);
  readonly details = signal<DetailsItem[]>([]);
  readonly loading = signal(true);
  readonly loaded = signal(false);

  ngOnInit(): void {
    this.loadUser();
  }

  private loadUser(): void {
    const id = this.route.snapshot.paramMap.get('id') ?? this.authService.currentUser()?.id;
    
    if (!id) {
      this.loading.set(false);
      this.toast.danger('Identifiant introuvable.');
      return;
    }

    this.loading.set(true);
    this.userService.findById(id).subscribe({
      next: (res) => {
        this.user.set(res.data ?? null);

        if (res.data) {
          this.details.set([
            { label: 'Pseudo', value: res.data.username },
            { label: 'Nom', value: res.data.lastName },
            { label: 'Prénom', value: res.data.firstName },
            { label: 'N° de téléphone', value: res.data.phone },
            { label: 'Email', value: res.data.email },
          ]);
        }

        this.loaded.set(true);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger("Impossible de charger l'utilisateur.");
      },
    });
  }
}
