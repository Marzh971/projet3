import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { Router, RouterLink } from '@angular/router';
import { buildErrorMessage } from '../../../../shared/services/message';
import { ToastService } from '../../../../shared/services/toast';
import { CategoryService } from '../../services/category-service';
import { CategoryRequestDTO } from '../../dtos/category-request.dto';

@Component({
  selector: 'app-category-form',
  templateUrl: './category-form.page.html',
  imports: [ReactiveFormsModule, InputComponent, SectionComponent, ButtonComponent, RouterLink],
})
export class CategoryFormPage {
  private readonly fb = inject(FormBuilder);
  private readonly categoryService = inject(CategoryService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);
  protected CategoryCreateForm = this.fb.nonNullable.group({
    nameCategory: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
  });

  submit(): void {
    if (this.CategoryCreateForm.invalid) {
      const message = this.buildErrorMessage();

      this.toast.danger(message);

      return;
    }

    const dto: CategoryRequestDTO = {
      nameCategory: this.CategoryCreateForm.getRawValue().nameCategory,
    };
    this.categoryService.create(dto).subscribe({
      next: (response) => {

        this.toast.success(response.message ?? 'Catégorie créée avec succès.');

        this.router.navigate(['/category']);
      },

      error: (err) => {
        console.error('Erreur création catégorie :', err);

        const message = err?.error?.message ?? 'Une erreur est survenue lors de la création.';

        this.toast.danger(message);
      },
    });
  }

  private readonly fieldLabels: Record<string, string> = {
    nameCategory: 'Nom',
  };

  private buildErrorMessage(): string {
    return buildErrorMessage(
      this.CategoryCreateForm,

      this.fieldLabels,
    );
  }
}
