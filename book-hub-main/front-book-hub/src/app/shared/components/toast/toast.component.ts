import { Component, inject } from '@angular/core'
import { ToastService } from '../../services/toast';

@Component({
  selector: 'bh-toast',
  standalone: true,
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.css'
})
export class ToastComponent {
  toast = inject(ToastService);
}
