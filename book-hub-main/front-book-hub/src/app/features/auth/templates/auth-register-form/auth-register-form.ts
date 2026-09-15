import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { AuthRegisterDto } from '../../dtos/auth-register.dto';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { ToastService } from '../../../../shared/services/toast';
import { buildErrorMessage } from '../../../../shared/services/message';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'auth-register-form',
  templateUrl: 'auth-register-form.html',
  imports: [ReactiveFormsModule, SectionComponent, InputComponent, ButtonComponent],
})
export class AuthRegisterFormComponent {
  private readonly authService = inject(AuthService);
  private readonly toast = inject(ToastService);
  private readonly router = inject(Router);


  registerForm = new FormGroup({
    username: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
        Validators.pattern(/^[a-zA-Z0-9_]+$/),
      ],
    }),

    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),

    firstName: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(50),
        Validators.pattern(/^[a-zA-ZÀ-ÿ\s-]+$/),
      ],
    }),

    lastName: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(50),
        Validators.pattern(/^[a-zA-ZÀ-ÿ\s-]+$/),
      ],
    }),

    phone: new FormControl('', [Validators.pattern(/^0[1-9](\s?\d{2}){4}$/)]),

    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(12), Validators.maxLength(30)],
    }),

    passwordConfirmation: new FormControl('',[Validators.required]),
  });

  submit() {
    if (this.registerForm.invalid) {
      this.toast.danger(this.buildErrorMessage());
      return;
    }
    if (
      this.registerForm.get('password')?.value !==
      this.registerForm.get('passwordConfirmation')?.value
    ) {
      this.toast.danger('Les mots de passe ne correspondent pas.');
      return;
    }

    const rawValue = this.registerForm.getRawValue();

    const dto: AuthRegisterDto = {
      username: rawValue.username,
      email: rawValue.email,
      firstName: rawValue.firstName,
      lastName: rawValue.lastName,
      phone: rawValue.phone,
      password: rawValue.password,
    };

    this.authService.register(dto).subscribe({
      next: (response) => {
        this.toast.success('Compte créé avec succès !');
        this.router.navigate(['/auth/login']);
      },
      error: (err: HttpErrorResponse) => {
        const message = err.error?.message ?? 'Une erreur est survenue, veuillez réessayer.';
        this.toast.danger(message);
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
    return buildErrorMessage(this.registerForm, this.fieldLabels);
  }
}
