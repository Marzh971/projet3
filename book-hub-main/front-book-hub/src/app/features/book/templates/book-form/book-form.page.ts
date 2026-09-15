import { Component, inject, signal } from '@angular/core';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../../../shared/services/toast';
import { BookService } from '../../services/book.service';
import { buildErrorMessage } from '../../../../shared/services/message';
import { BookRequestDto } from '../../dtos/book-request.dto';
import { SelectComponent, SelectOption } from '../../../../shared/components/form/select/select.component';
import { CategoryService } from '../../../category/services/category-service';
import { PublisherService } from '../../../publisher/services/publisher.service';
import { MultiSelectComponent } from '../../../../shared/components/form/multi-select/multi-select.component';
import { AuthorService } from '../../../author/services/author.service';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-book-form',
  imports: [
    SectionComponent,
    ButtonComponent,
    InputComponent,
    ReactiveFormsModule,
    RouterLink,
    SelectComponent,
    MultiSelectComponent,
  ],
  templateUrl: './book-form.page.html',
})
export class BookFormPage {
  private readonly bookService = inject(BookService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);
  private readonly publisherService = inject(PublisherService);
  private readonly categoryService = inject(CategoryService);
  private readonly authorService = inject(AuthorService);

  publisher = signal<SelectOption[]>([]);
  categories = signal<SelectOption[]>([]);
  authors = signal<SelectOption[]>([]);

  readonly bookCreateForm = this.fb.nonNullable.group({
    title: ['', [Validators.required]],
    isbn: ['', Validators.required],
    description: [''],
    publicationDate: ['', [Validators.required,this.publicationDateValidator()]],
    pageNumber: ['1', [Validators.required, Validators.min(1)]],
    publisher: [null as number | null],
    categories: [[] as number[]],
    authors: [[] as number[]],
    copyNumber: ['1', [Validators.required, Validators.min(1)]],
  });

  constructor() {
    this.loadPublisher();
    this.loadCategories();
    this.loadAuthors();
  }

  submit(): void {
    if (this.bookCreateForm.invalid) {
      this.buildErrorMessage();
      return;
    }

    const rawValues = this.bookCreateForm.getRawValue();

    const dto: BookRequestDto = {
      title: rawValues.title,
      isbn: rawValues.isbn,
      description: rawValues.description,
      publicationDate: rawValues.publicationDate,
      pageNumber: Number(rawValues.pageNumber),
      publisherId: rawValues.publisher ? Number(rawValues.publisher) : null,
      categoryIds: rawValues.categories.map(Number),
      authorIds: rawValues.authors.map(Number),
      copyNumber: Number(rawValues.copyNumber),
    };

    this.bookService.create(<BookRequestDto>dto).subscribe({
      next: (response) => {
        if (response.code === '200') {
          this.toast.success(response.message);
          this.router.navigate(['/book']);
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
    title: 'Titre',
    isbn: 'ISBN',
    publicationDate: 'Date de publication',
    pageNumber: 'Nombre de page',
    copyNumber: "Nombre d'exemplaires",
  };

  private buildErrorMessage(): void {
    const message = buildErrorMessage(this.bookCreateForm, this.fieldLabels);
    this.toast.danger(message); // <-- Indispensable pour voir pourquoi ça bloque
  }

  loadPublisher() {
    this.publisherService.findAll().subscribe((response) => {
      if (!response.data) {
        this.publisher.set([]);
        return;
      }

      this.publisher.set([
        {
          value: '',
          label: 'Sélectionner un éditeur',
        },
        ...response.data.map((publisher) => ({
          value: Number(publisher.id),
          label: publisher.name,
        })),
      ]);
    });
  }

  loadCategories() {
    this.categoryService.findAll().subscribe((response) => {
      if (!response.data) {
        this.categories.set([]);
        return;
      }

      this.categories.set([
        ...response.data.map((category) => ({
          value: Number(category.id),
          label: category.nameCategory,
        })),
      ]);
    });
  }

  loadAuthors() {
    this.authorService.findAll().subscribe((response) => {
      if (!response.data) {
        this.authors.set([]);
        return;
      }
      this.authors.set([
        ...response.data.map((author) => ({
          value: Number(author.id),
          label: `${author.firstName} ${author.lastName}`,
        })),
      ]);
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

}
