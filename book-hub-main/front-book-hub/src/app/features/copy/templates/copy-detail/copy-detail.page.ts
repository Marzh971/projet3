import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CopyService } from '../../services/copy.service';
import { CopyResponseDto } from '../../dtos/copy-response.dto';
import { ToastService } from '../../../../shared/services/toast';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { DetailsItem } from '../../../../shared/components/details/details.model';
import { CardComponent } from '../../../../shared/components/card/card.component';

@Component({
  selector: 'app-copy-detail',
  standalone: true,
  templateUrl: './copy-detail.page.html',
  imports: [SectionComponent, CardComponent],
})
export class CopyDetailPage implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly copyService = inject(CopyService);
  private readonly toast = inject(ToastService);

  readonly copy = signal<CopyResponseDto | null>(null);
  readonly details = signal<DetailsItem[]>([]);
  readonly loading = signal(true);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      this.toast.danger('Identifiant introuvable.');
      return;
    }

    this.loading.set(true);

    this.copyService.findById(Number(id)).subscribe({
      next: (res) => {
        this.copy.set(res.data ?? null);

        if (res.data) {
          this.details.set([
            { label: 'Code-barre', value: res.data.barcode },
            { label: 'Auteur', value: res.data.bookAuthor },
            { label: 'Éditeur', value: res.data.bookPublisher },
            {
              label: "Date d'achat",
              value: new Date(res.data.purchaseDate).toLocaleDateString('fr-FR'),
            },
          ]);
        }

        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger("Impossible de charger l'exemplaire.");
      },
    });
  }
}
