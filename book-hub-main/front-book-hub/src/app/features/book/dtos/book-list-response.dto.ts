import { AuthorListResponseDto } from '../../author/dtos/author-list-response.dto';
import { CategoryResponseDTO } from '../../category/dtos/category-response.dto';

export interface BookListResponseDto {
  isbn: string;
  title: string;
  cover: string;
  authors: AuthorListResponseDto[];
  categories: CategoryResponseDTO[];
}
