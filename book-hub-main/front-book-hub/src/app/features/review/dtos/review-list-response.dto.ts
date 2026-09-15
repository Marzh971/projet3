export interface ReviewListResponseDto {
  id: number;
  rate: number;
  comment: string;
  username: string;
  bookIsbn?: string;
  dateReview?: string;
}
