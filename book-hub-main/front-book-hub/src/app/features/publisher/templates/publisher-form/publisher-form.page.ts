import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../../shared/services/toast';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PublisherService } from '../../services/publisher.service';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';

@Component({
  selector: 'app-publisher-form',
  templateUrl: './publisher-form.page.html',
  imports: [ReactiveFormsModule, InputComponent, SectionComponent, ButtonComponent, RouterLink],
})
export class PublisherFormPage implements OnInit {
  private readonly publisherService = inject(PublisherService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  private publisherId: string | null = null;

  readonly publisherCreateForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
  });

  get isEditMode(): boolean {
    return this.publisherId !== null;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) return;

    this.publisherId = idParam;

    this.publisherService.findById(this.publisherId).subscribe({
      next: (response) => {
        if (response.data) {
          this.publisherCreateForm.patchValue({
            name: response.data.name,
          });
        } else {
          this.toast.danger(response.message ?? 'Éditeur introuvable.');
          this.router.navigate(['/publisher']);
        }
      },
      error: (err) => {
        console.error(err);
        this.toast.danger("Une erreur est survenue lors du chargement de l'éditeur.");
        this.router.navigate(['/publisher']);
      },
    });
  }

  submit(): void {
    if (this.publisherCreateForm.invalid) {
      this.toast.danger(this.buildErrorMessage());
      return;
    }

    const dto = this.publisherCreateForm.getRawValue();

    const request$ = this.isEditMode
      ? this.publisherService.update(this.publisherId!, dto)
      : this.publisherService.create(dto);

    request$.subscribe({
      next: (response) => {
        this.toast.success(response.message ?? 'Opération réussie.');
        this.router.navigate(['/publisher']);
      },
      error: (err) => {
        console.error(err);

        const message =
          err?.error?.message ??
          (this.isEditMode
            ? 'Une erreur est survenue lors de la modification.'
            : 'Une erreur est survenue lors de la création.');

        this.toast.danger(message);
      },
    });
  }

  private readonly fieldLabels: Record<string, string> = {
    name: 'Nom',
  };

  private buildErrorMessage(): string {
    const messages: string[] = [];

    Object.entries(this.publisherCreateForm.controls).forEach(([key, control]) => {
      if (!control.errors) return;
      const label = this.fieldLabels[key] ?? key;

      if (control.errors['required']) {
        messages.push(`${label} est requis.`);
      }
      if (control.errors['minlength']) {
        messages.push(
          `${label} doit contenir au moins ${control.errors['minlength'].requiredLength} caractères.`,
        );
      }
      if (control.errors['maxlength']) {
        messages.push(
          `${label} ne doit pas dépasser ${control.errors['maxlength'].requiredLength} caractères.`,
        );
      }
    });

    return messages.length > 0 ? messages.join(' ') : 'Le formulaire contient des erreurs.';
  }
}
