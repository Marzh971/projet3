import {Injectable, signal} from '@angular/core';

export type ToastVariant = 'info' | 'success' | 'danger';

export interface Toast {
  id:       number;
  message:  string;
  variant:  ToastVariant;
  duration: number;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  toasts = signal<Toast[]>([]);
  private counter = 0;

  show(message: string, variant: ToastVariant = 'info', duration = 5000) {
    const id = ++this.counter;
    this.toasts.update(t => [...t, { id, message, variant, duration }]);
    setTimeout(() => this.dismiss(id), duration);
  }

  dismiss(id: number) {
    this.toasts.update(t => t.filter(toast => toast.id !== id));
  }

  success(message: string) { this.show(message, 'success'); }
  danger(message: string)  { this.show(message, 'danger'); }
  info(message: string)    { this.show(message, 'info'); }
}
