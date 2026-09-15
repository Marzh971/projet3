import { Component, computed, inject, signal } from '@angular/core';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { BookService } from '../../services/book.service';
import { ToastService } from '../../../../shared/services/toast';
import { BookListResponseDto } from '../../dtos/book-list-response.dto';
import { CardComponent } from '../../../../shared/components/card/card.component';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { InputComponent } from '../../../../shared/components/form/input/input.component';
import {
  SelectComponent,
  SelectOption,
} from '../../../../shared/components/form/select/select.component';
import { CategoryService } from '../../../category/services/category-service';
import { FormsModule } from '@angular/forms';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-book-list',
  imports: [
    EmptyStateComponent,
    CardComponent,
    SectionComponent,
    InputComponent,
    SelectComponent,
    ButtonComponent,
    FormsModule,
    PaginationComponent,
    RouterLink,
  ],
  templateUrl: './book-list.page.html',
  styleUrl: './book-list.page.css',
})
export class BookListPage {
  private readonly bookService = inject(BookService);
  private readonly toast = inject(ToastService);
  private readonly categoryService = inject(CategoryService);
  private readonly authService = inject(AuthService);


  readonly hasRoleLibrarian = this.authService.hasRole('ROLE_LIBRARIAN');

  categories = signal<SelectOption[]>([]);
  disponibilities = signal<SelectOption[]>([]);

  // Contient uniquement les livres de la page actuelle renvoyés par le Back-end
  books = signal<BookListResponseDto[]>([]);
  readonly loading = signal(true);

  protected text = '';
  protected categoryId?: number;

  // Pagination (Spring Boot commence à 0)
  page = signal(0);
  pageSize = signal(5);

  // Nombre total d'éléments en base (fourni par le Back-end)
  totalElements = signal(0);

  // Calcul dynamique du nombre total de pages pour l'interface
  readonly totalPages = computed(() => {
    const size = this.pageSize();
    if (size === 0) return 0;
    return Math.ceil(this.totalElements() / size);
  });

  readonly total = computed(() => this.totalElements());

  constructor() {
    this.loadBooks();
    this.loadCategories();
    this.disponibilities.set([
      { value: '', label: 'Toutes les disponibilités' },
      { value: 'available', label: 'Disponible' },
      { value: 'unavailable', label: 'Non disponible' },
    ]);
  }

  // Chargement des livres avec pagination et filtres serveur
  private loadBooks(): void {
    this.loading.set(true);

    this.bookService
      .getAllFilter(this.text, this.categoryId, this.page(), this.pageSize())
      .subscribe({
        next: (res) => {
          this.books.set(res.data?.content ?? []);
          this.totalElements.set(res.data?.totalElements ?? 0);
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
          this.toast.danger('Impossible de charger les livres.');
        },
      });
  }

  loadCategories() {
    this.categoryService.findAll().subscribe((response) => {
      if (!response.data) {
        this.categories.set([]);
        return;
      }

      this.categories.set([
        {
          value: '',
          label: 'Toutes les catégories',
        },
        ...response.data.map((category) => ({
          value: Number(category.id),
          label: category.nameCategory,
        })),
      ]);
    });
  }

  getAuthors(book: BookListResponseDto): string {
    return book.authors.map((author) => `${author.firstName} ${author.lastName}`).join(', ');
  }

  getCategories(book: BookListResponseDto): string {
    return book.categories.map((category) => category.nameCategory).join(', ');
  }

  columns(): number {
    if (window.innerWidth < 480) return 1;
    if (window.innerWidth < 768) return 2;
    if (window.innerWidth < 1200) return 3;
    return 5;
  }

  protected onFilter(): void {
    this.page.set(0);
    this.loadBooks();
  }

  protected onPageChange(newPageHuman: number): void {
    const springPageIndex = newPageHuman - 1;
    this.page.set(springPageIndex);
    this.loadBooks();
  }

  protected onPageSizeChange(newSize: number): void {
    this.pageSize.set(newSize);
    this.page.set(0);
    this.loadBooks();
  }
}
