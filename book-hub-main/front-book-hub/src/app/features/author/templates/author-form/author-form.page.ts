import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../../shared/services/toast';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthorService } from '../../services/author.service';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';

@Component({
  selector: 'app-author-form',
  templateUrl: './author-form.page.html',
  imports: [ReactiveFormsModule, InputComponent, SectionComponent, ButtonComponent, RouterLink],
})
export class AuthorFormPage implements OnInit {
  private readonly authorService = inject(AuthorService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  private authorId: string | null = null;

  readonly authorCreateForm = this.fb.nonNullable.group({
    firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
    lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
  });

  get isEditMode(): boolean {
    return this.authorId !== null;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) return;
    this.authorId = idParam;
    this.authorService.findById(this.authorId).subscribe({
      next: (response) => {
        if (response.data) {

          this.authorCreateForm.patchValue({
            firstName: response.data.firstName,
            lastName: response.data.lastName,
          });
        } else {
          this.toast.danger(response.message ?? 'Auteur introuvable.');
          this.router.navigate(['/author']);
        }
      },
      error: (err) => {
        console.error(err);
        this.toast.danger("Une erreur est survenue lors du chargement de l'auteur.");
        this.router.navigate(['/author']);
      },
    });
  }

  submit(): void {
    if (this.authorCreateForm.invalid) {
      this.toast.danger(this.buildErrorMessage());
      return;
    }

    const dto = this.authorCreateForm.getRawValue();

    const request$ = this.isEditMode
      ? this.authorService.update(Number(this.authorId), dto)
      : this.authorService.create(dto);

    request$.subscribe({
      next: (response) => {

        this.toast.success(response.message ?? 'Opération réussie.');
        this.router.navigate(['/author']);
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
    firstName: 'Prénom',
    lastName: 'Nom',
  };

  private buildErrorMessage(): string {
    const messages: string[] = [];

    Object.entries(this.authorCreateForm.controls).forEach(([key, control]) => {
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
