export interface CopyResponseDto {
  id: string;
  barcode: string;
  bookIsbn: string;
  bookTitle: string;
  bookAuthor: string;
  bookPublisher: string;
  bookCategories: string[];
  purchaseDate: Date;
}
