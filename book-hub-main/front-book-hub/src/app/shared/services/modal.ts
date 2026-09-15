import { Injectable, signal, TemplateRef } from '@angular/core';

export interface ModalConfig {
  title:      string;
  template?:  TemplateRef<any>;
  content?:   string;
  size?:      'sm' | 'md' | 'lg';
  onConfirm?: () => void;
  onCancel?:  () => void;
}

@Injectable({ providedIn: 'root' })
export class ModalService {
  config  = signal<ModalConfig | null>(null);
  visible = signal(false);

  open(config: ModalConfig) {
    this.config.set(config);
    this.visible.set(true);
  }

  close() {
    this.visible.set(false);
    setTimeout(() => this.config.set(null), 200);
  }

  confirm() {
    this.config()?.onConfirm?.();
    this.close();
  }

  cancel() {
    this.config()?.onCancel?.();
    this.close();
  }
}
