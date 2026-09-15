import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CopyService } from '../../services/copy.service';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../../../shared/services/toast';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { buildErrorMessage } from '../../../../shared/services/message';
import { CopyRequestDto } from '../../dtos/copy-request.dto';

@Component({
  selector: 'app-copy-form',
  standalone: true,
  templateUrl: './copy-form.page.html',
  imports: [ReactiveFormsModule, RouterLink, SectionComponent, ButtonComponent, InputComponent],
})
export class CopyFormPage {
  private readonly copyService = inject(CopyService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  readonly copyForm = this.fb.nonNullable.group({
    barcode: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(8)]],
    bookISBN: ['', [Validators.required]],
  });

  private readonly fieldLabels: Record<string, string> = {
    barcode: 'Code-barre',
    bookISBN: 'ISBN',
  };

  submit(): void {
    if (this.copyForm.invalid) {
      this.buildErrorMessage();
      return;
    }

    const raw = this.copyForm.getRawValue();

    const dto: CopyRequestDto = {
      barcode: raw.barcode,
      bookIsbn: raw.bookISBN,
    };

    this.copyService.save(dto).subscribe({
      next: (res) => {
        this.toast.success(res.message ?? 'Exemplaire créé avec succès.');
        this.router.navigate(['/copies']);
      },
      error: (err) => {
        const message =
          err?.error?.message ?? "Une erreur est survenue lors de la création de l'exemplaire.";
        this.toast.danger(message);
      },
    });
  }

  private buildErrorMessage(): void {
    const message = buildErrorMessage(this.copyForm, this.fieldLabels);
    this.toast.danger(message);
  }
}
