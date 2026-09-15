import { AuthorListResponseDto } from '../../author/dtos/author-list-response.dto';
import { CategoryResponseDTO } from '../../category/dtos/category-response.dto';
import { PublisherResponseDto } from '../../publisher/dtos/publisher-response.dto';
import { ReviewListResponseDto } from '../../review/dtos/review-list-response.dto';

export interface BookResponseDto {
  isbn: string;
  title: string;
  description: string;
  cover: string;
  publicationDate: string;
  pageNumber: number;
  publisher: PublisherResponseDto;
  authors: AuthorListResponseDto[];
  categories: CategoryResponseDTO[];
  reviews: ReviewListResponseDto[];
}
