import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

export function buildErrorMessage(
    form: FormGroup,
    fieldLabels: Record<string, string>,
  ): string {
    const messages: string[] = [];

    Object.entries(form.controls).forEach(([key, control]) => {
      if (!control.errors) return;
      const label = fieldLabels[key] ?? key;

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
      if (control.errors['email']) {
        messages.push(`${label} doit être une adresse email valide.`);
      }
    });

    return messages.length > 0 ? messages.join(' ') : 'Le formulaire contient des erreurs.';

}
