import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../../shared/services/toast';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { buildErrorMessage } from '../../../../shared/services/message';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.page.html',
  imports: [ReactiveFormsModule, InputComponent, SectionComponent, ButtonComponent, RouterLink],
})
export class UserFormPage {
  private readonly userService = inject(UserService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  readonly userCreateForm = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    firstName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]],
    password: ['', [Validators.required, Validators.minLength(12), Validators.maxLength(30)]],
    passwordConfirmation: ['', Validators.required],
  });

  submit(): void {
    if (this.userCreateForm.invalid) {
      this.buildErrorMessage();
      return;
    }
    if (
      this.userCreateForm.get('password')?.value !==
      this.userCreateForm.get('passwordConfirmation')?.value
    ) {
      this.toast.danger('Les mots de passe ne correspondent pas.');
      return;
    }

    const { passwordConfirmation, ...dto } = this.userCreateForm.getRawValue();

    this.userService.create(dto).subscribe({
      next: (response) => {
        if (response.code === '200') {
          this.toast.success(response.message);
          this.router.navigate(['/users']);
        } else {
          this.toast.danger(response.message);
        }
      },
      error: (err) => {
        console.error(err);
        this.toast.danger('Une erreur est survenue lors de la création.');
      },
    });
  }

  private readonly fieldLabels: Record<string, string> = {
    username: 'Pseudo',
    firstName: 'Prénom',
    lastName: 'Nom',
    email: 'Email',
    phone: 'Téléphone',
    password: 'Mot de passe',
    passwordConfirmation: 'Confirmation du mot de passe',
  };

  private buildErrorMessage(): string {
    return buildErrorMessage(this.userCreateForm, this.fieldLabels);
  }
}
