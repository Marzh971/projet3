export interface LoanRequestDto {
  id?: number;
  bookIsbn: string;
  userId: string;
  dateRetrieved: string;
  dateReturned: string | null;
}
