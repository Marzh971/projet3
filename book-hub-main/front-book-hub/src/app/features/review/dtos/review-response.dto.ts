export interface ReviewResponse {
  id: number;
  rate: number;
  comment: string;
  username: string;
  bookIsbn?: string;
  dateReview?: string;
}
