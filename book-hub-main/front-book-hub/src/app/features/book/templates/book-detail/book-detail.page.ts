import { Component, inject, signal, TemplateRef, viewChild } from '@angular/core';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { BookService } from '../../services/book.service';
import { ToastService } from '../../../../shared/services/toast';
import { BookResponseDto } from '../../dtos/book-response.dto';
import { ActivatedRoute, Router } from '@angular/router';
import { BadgeComponent } from '../../../../shared/components/badge/badge.component';
import { DetailsComponent } from '../../../../shared/components/details/details.component';
import { DetailsItem } from '../../../../shared/components/details/details.model';
import { ModalService } from '../../../../shared/services/modal';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { ReviewService } from '../../../review/services/review.service';
import {
  DropdownComponent,
  DropdownItem,
} from '../../../../shared/components/dropdown/dropdown.component';
import { BookRequestDto } from '../../dtos/book-request.dto';
import { CategoryService } from '../../../category/services/category-service';
import {
  SelectComponent,
  SelectOption,
} from '../../../../shared/components/form/select/select.component';
import { AuthorService } from '../../../author/services/author.service';
import { PublisherService } from '../../../publisher/services/publisher.service';

type BookReview = BookResponseDto['reviews'][number];
import { MultiSelectComponent } from '../../../../shared/components/form/multi-select/multi-select.component';
import { AuthService } from '../../../auth/services/auth.service';
import { LoanService } from '../../../loan/services/loan.service';
import { LoanRequestDto } from '../../../loan/dtos/loan-request.dto';
import { UserService } from '../../../user/services/user.service';
import { ReservationsService } from '../../../reservations/services/reservations-service';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [
    SectionComponent,
    BadgeComponent,
    DetailsComponent,
    ReactiveFormsModule,
    InputComponent,
    ButtonComponent,
    DropdownComponent,
    MultiSelectComponent,
    SelectComponent,
  ],
  templateUrl: './book-detail.page.html',
  styleUrls: ['./book-detail.page.css'],
})
export class BookDetailPage {
  private readonly bookService = inject(BookService);
  private readonly reservationService = inject(ReservationsService);
  private readonly reviewService = inject(ReviewService);
  private readonly loanService = inject(LoanService);
  private readonly userService = inject(UserService);
  private readonly authService = inject(AuthService);
  private readonly toast = inject(ToastService);
  private readonly modal = inject(ModalService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly categoryService = inject(CategoryService);
  readonly authorService = inject(AuthorService);
  readonly publisherService = inject(PublisherService);

  readonly book = signal<BookResponseDto | null>(null);
  readonly details = signal<DetailsItem[]>([]);
  readonly loading = signal(true);
  readonly submitting = signal(false);
  readonly deleting = signal(false);
  readonly updating = signal(false);
  readonly bookUpdating = signal(false);
  readonly reserving = signal(false);
  readonly loaning = signal(false);

  readonly hasRoleLibrarian = this.authService.hasRole('ROLE_LIBRARIAN');

  readonly page = signal(1);

  readonly categories = signal<SelectOption[]>([]);
  readonly authors = signal<SelectOption[]>([]);
  readonly publisher = signal<SelectOption[]>([]);
  readonly users = signal<SelectOption[]>([]);

  reviewToDelete: BookReview | null = null;
  reviewToUpdate: BookReview | null = null;

  readonly reviewModalTplt = viewChild.required<TemplateRef<unknown>>('reviewModalTplt');
  readonly deleteModalTplt = viewChild.required<TemplateRef<unknown>>('deleteModalTplt');
  readonly updateModalTplt = viewChild.required<TemplateRef<unknown>>('updateModalTplt');
  readonly bookUpdateModalTplt = viewChild.required<TemplateRef<unknown>>('bookUpdateModalTplt');
  readonly loanModalTplt = viewChild.required<TemplateRef<unknown>>('loanModalTplt');

  readonly reviewForm = this.fb.nonNullable.group({
    rate: [5, [Validators.required, Validators.min(1), Validators.max(5)]],
    comment: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(500)]],
  });

  readonly updateForm = this.fb.nonNullable.group({
    rate: [5, [Validators.required, Validators.min(1), Validators.max(5)]],
    comment: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(500)]],
  });

  readonly loanForm = this.fb.nonNullable.group({
    userId: [null as string | null, [Validators.required]],
  });

  constructor() {
    this.loadBook();
    this.loadAuthors();
    this.loadCategories();
    this.loadPublisher();
    this.loadUsers();
  }

  private loadBook(): void {
    const isbn = this.route.snapshot.paramMap.get('isbn');

    if (!isbn) {
      this.toast.danger('ISBN introuvable.');
      return;
    }

    this.loading.set(true);
    this.bookService.getByIsbn(isbn).subscribe({
      next: (res) => {
        this.book.set(res.data ?? null);

        if (res.data) {
          this.details.set([
            { label: 'ISBN', value: res.data.isbn },
            {
              label: 'Publication',
              value: new Date(res.data.publicationDate).toLocaleDateString('fr-FR'),
            },
            { label: 'Pages', value: res.data.pageNumber.toString() },
            { label: 'Éditeur', value: res.data.publisher?.name ?? 'Inconnu' },
          ]);
        }

        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.danger('Impossible de charger le livre.');
      },
    });
  }

  hasRatings(reviews: { rate?: number | null }[]): boolean {
    return reviews.some((review) => review.rate != null);
  }

  countRatings(reviews: { rate?: number | null }[]): number {
    return reviews.filter((review) => review.rate != null).length;
  }

  getAverageRate(reviews: { rate?: number | null }[]): number {
    const ratedReviews = reviews.filter((review) => review.rate != null);

    if (ratedReviews.length === 0) {
      return 0;
    }

    const total = ratedReviews.reduce((sum, review) => sum + (review.rate ?? 0), 0);

    return Math.round((total / ratedReviews.length) * 10) / 10;
  }

  openReviewModal(): void {
    this.reviewForm.reset({
      rate: 5,
      comment: '',
    });

    this.modal.open({
      title: 'Laisser un avis',
      template: this.reviewModalTplt(),
      size: 'sm',
    });
  }

  closeReviewModal(): void {
    this.modal.close();
  }

  submitReview(): void {
    if (this.reviewForm.invalid) {
      this.toast.danger('Veuillez renseigner une note (1 à 5) et un commentaire.');
      return;
    }

    const book = this.book();
    if (!book) return;

    const dto = {
      bookIsbn: book.isbn,
      ...this.reviewForm.getRawValue(),
    };

    console.log(dto);
    this.submitting.set(true);

    this.reviewService.create(dto).subscribe({
      next: (res) => {
        this.submitting.set(false);
        this.toast.success(res.message ?? 'Avis ajouté.');
        this.modal.close();
        this.loadBook();
      },
      error: (err) => {
        this.submitting.set(false);
        const message = err?.error?.message ?? "Une erreur est survenue lors de l'ajout de l'avis.";
        this.toast.danger(message);
      },
    });
  }

  actionsFor(review: BookReview): DropdownItem[] {
    return [
      {
        label: 'Voir',
        icon: 'heroEye',
        action: () => {
          this.toast.info(`Avis noté ${review.rate}/5`);
        },
      },
      {
        label: 'Modifier',
        icon: 'heroPencil',
        action: () => {
          this.openUpdateModal(review);
        },
      },
      {
        label: 'Supprimer',
        icon: 'heroTrash',
        danger: true,
        action: () => {
          this.openDeleteModal(review);
        },
      },
    ];
  }

  private openUpdateModal(review: BookReview): void {
    this.reviewToUpdate = review;

    this.updateForm.patchValue({
      rate: review.rate,
      comment: review.comment,
    });

    this.modal.open({
      title: 'Modifier un avis',
      template: this.updateModalTplt(),
      size: 'sm',
    });
  }

  submitUpdate(): void {
    if (this.updateForm.invalid || !this.reviewToUpdate) {
      return;
    }

    this.updating.set(true);

    const review = this.reviewToUpdate;
    const payload = this.updateForm.getRawValue();

    this.reviewService.update(review.id.toString(), payload).subscribe({
      next: (res) => {
        this.updating.set(false);
        this.toast.success(res.message ?? 'Avis modifié.');
        this.modal.close();
        this.reviewToUpdate = null;
        this.loadBook();
      },
      error: (err) => {
        this.updating.set(false);
        const message = err?.error?.message ?? 'Modification impossible.';
        this.toast.danger(message);
      },
    });
  }

  private openDeleteModal(review: BookReview): void {
    this.reviewToDelete = review;

    this.modal.open({
      title: 'Supprimer cet avis',
      template: this.deleteModalTplt(),
      size: 'sm',
    });
  }

  cancelDelete(): void {
    this.reviewToDelete = null;
    this.modal.close();
  }

  confirmDelete(): void {
    if (!this.reviewToDelete) {
      return;
    }

    this.deleting.set(true);

    const review = this.reviewToDelete;

    this.reviewService.delete(review.id.toString()).subscribe({
      next: (res) => {
        this.deleting.set(false);
        this.toast.success(res.message ?? 'Avis supprimé.');
        this.modal.close();
        this.reviewToDelete = null;
        this.loadBook();
      },
      error: (err) => {
        this.deleting.set(false);
        const message = err?.error?.message ?? "Impossible de supprimer l'avis.";
        this.toast.danger(message);
      },
    });
  }

  setInactive(): void {
    const book = this.book();

    if (!book) {
      return;
    }

    this.bookService.setInactive(book.isbn).subscribe({
      next: (res) => {
        if (res.code === '200') {
          this.toast.success(res.message);
          this.router.navigate(['/book']);
        } else {
          this.toast.danger(res.message);
        }
      },
      error: () => {
        this.toast.danger('Une erreur est survenue lors de la mise à jour du livre.');
      },
    });
  }

  // modal Book

  readonly bookUpdateForm = this.fb.nonNullable.group({
    title: ['', [Validators.required]],
    description: [''],
    publicationDate: ['', [Validators.required, this.publicationDateValidator()]],
    pageNumber: [1, [Validators.required, Validators.min(1)]],
    publisher: [null as number | null],
    categories: [[] as number[]],
    authors: [[] as number[]],
  });

  openBookUpdateModal(): void {
    const book = this.book();
    if (!book) return;

    this.bookUpdateForm.reset({
      title: book.title,
      description: book.description ?? '',
      publicationDate: book.publicationDate?.substring(0, 10) ?? '',
      pageNumber: book.pageNumber,
      publisher: book.publisher ? Number(book.publisher.id) : null,
      categories: book.categories.map((category) => Number(category.id)),
      authors: book.authors.map((author) => Number(author.id)),
    });

    this.modal.open({
      title: 'Modifier le livre',
      template: this.bookUpdateModalTplt(),
      size: 'md',
    });
  }

  closeBookUpdateModal(): void {
    this.modal.close();
  }

  submitBookUpdate(): void {
    if (this.bookUpdateForm.invalid) {
      this.toast.danger('Veuillez vérifier les champs du formulaire.');
      return;
    }

    const book = this.book();
    if (!book) return;

    const raw = this.bookUpdateForm.getRawValue();

    const dto: BookRequestDto = {
      title: raw.title,
      isbn: book.isbn,
      description: raw.description,
      publicationDate: raw.publicationDate,
      pageNumber: Number(raw.pageNumber) || 1,
      copyNumber: 1,
      publisherId: raw.publisher ? Number(raw.publisher) : null,
      categoryIds: raw.categories.map(Number),
      authorIds: raw.authors.map(Number),
    } as BookRequestDto;

    this.bookUpdating.set(true);

    this.bookService.update(book.isbn, dto).subscribe({
      next: (res) => {
        this.bookUpdating.set(false);

        if (res.code === '200') {
          this.toast.success(res.message);
          this.modal.close();
          this.loadBook();
        } else {
          this.toast.danger(res.message);
        }
      },
      error: () => {
        this.bookUpdating.set(false);

        this.toast.danger('Une erreur est survenue lors de la mise à jour.');
      },
    });
  }

  loadPublisher(): void {
    this.publisherService.findAll().subscribe({
      next: (response) => {
        if (!response.data) {
          this.publisher.set([]);
          return;
        }

        this.publisher.set(
          response.data.map((publisher) => ({
            value: Number(publisher.id),
            label: publisher.name,
          })),
        );
      },
    });
  }

  loadCategories(): void {
    this.categoryService.findAll().subscribe({
      next: (response) => {
        console.log(response);
        if (!response.data) {
          this.categories.set([]);
          return;
        }

        this.categories.set(
          response.data.map((category) => ({
            value: Number(category.id),
            label: category.nameCategory,
          })),
        );
      },
    });
  }

  loadAuthors(): void {
    this.authorService.findAll().subscribe({
      next: (response) => {
        if (!response.data) {
          this.authors.set([]);
          return;
        }
        this.authors.set(
          response.data.map((author) => ({
            value: Number(author.id),
            label: `${author.firstName} ${author.lastName}`,
          })),
        );
      },
    });
  }
  //verif date
  publicationDateValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const publicationDate = new Date(control.value);
      const today = new Date();

      publicationDate.setHours(0, 0, 0, 0);
      today.setHours(0, 0, 0, 0);

      return publicationDate <= today ? null : { futureDate: true };
    };
  }

  openLoanModal(): void {
    const book = this.book();
    if (!book) return;

    this.loanForm.reset({ userId: null });

    this.modal.open({
      title: 'Déclarer un emprunt',
      template: this.loanModalTplt(),
      size: 'sm',
    });
  }

  closeLoanModal(): void {
    this.modal.close();
  }

  submitLoan(): void {
    if (this.loanForm.invalid) {
      this.toast.danger('Veuillez sélectionner un utilisateur.');
      return;
    }

    const book = this.book();
    if (!book) return;

    const { userId } = this.loanForm.getRawValue();

    const dto: LoanRequestDto = {
      bookIsbn: book.isbn,
      userId: userId as string,
      dateRetrieved: new Date().toISOString(),
      dateReturned: null,
    };

    this.loaning.set(true);

    console.log(dto);
    this.loanService.create(dto).subscribe({
      next: (res) => {
        this.loaning.set(false);
        this.toast.success(res.message ?? 'Emprunt déclaré.');
        this.modal.close();
        this.loadBook();
      },
      error: (err) => {
        this.loaning.set(false);
        const message =
          err?.error?.message ?? "Une erreur est survenue lors de la déclaration de l'emprunt.";
        this.toast.danger(message);
      },
    });
  }

  loadUsers(): void {
    this.userService.findAll().subscribe({
      next: (response) => {
        if (!response.data) {
          this.users.set([]);
          return;
        }

        this.users.set(
          response.data.map((user) => ({
            value: user.id,
            label: user.username,
          })),
        );
      },
    });
  }

  reserverLivre(): void {
    const book = this.book();
    if (!book) return;

    this.reserving.set(true);

    this.reservationService.create(book.isbn).subscribe({
      next: (res) => {
        this.reserving.set(false);
        this.toast.success(res.message ?? 'Réservation effectuée.');
      },
      error: (err) => {
        this.reserving.set(false);
        const message = err?.error?.message ?? 'Une erreur est survenue lors de la réservation.';
        this.toast.danger(message);
      },
    });
  }
}
