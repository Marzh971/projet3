
export interface BookRequestDto {
  isbn: string;
  title: string;
  description: string;
  publicationDate: string;
  copyNumber: number;
  pageNumber: number;
  publisherId: number | null;
  categoryIds: number[];
  authorIds: number[];
}
