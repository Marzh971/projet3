import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { AuthLoginDto } from '../../dtos/auth-login.dto';
import { ToastService } from '../../../../shared/services/toast';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { Router } from '@angular/router';

@Component({
  selector: 'auth-login-form',
  templateUrl: 'auth-login-form.html',
  imports: [ReactiveFormsModule, InputComponent, ButtonComponent, SectionComponent],
})
export class AuthLoginFormComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);

  loginForm = new FormGroup({
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(12), Validators.maxLength(30)],
    }),
  });

  submit() {
    const rawValue = this.loginForm.getRawValue();

    const dto: AuthLoginDto = {
      email: rawValue.email,
      password: rawValue.password,
    };
    this.authService.login(dto).subscribe({
      next: (response) => {
        this.toast.success("Connexion réussi !");
        this.router.navigate(['/']);
      },
      error: (err) =>
      this.toast.danger("Identifiants incorrects"),
    });
  }
}
