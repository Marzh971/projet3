import { inject, Injectable } from '@angular/core';
import { BookService } from '../../../book/services/book.service';
import { catchError, map, Observable, of, tap } from 'rxjs';
import { LoanService } from '../../../loan/services/loan.service';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private readonly bookService = inject(BookService);
  private readonly loanService = inject(LoanService);

  countAllBooks(): Observable<number> {
    return this.bookService.findAll().pipe(
      map((response) => response.data?.length ?? 0),
      catchError(() => of(0)),
    );
  }

  countAllPending() {
    return this.loanService.findAll().pipe(
      map((response) => response.data?.filter((loan) => loan.dateReturned === null).length ?? 0),
      catchError(() => of(0)),
    );
  }

  countOverdueLoans() {
    return this.loanService.findAll().pipe(
      map((response) => {
        const now = new Date().getTime();
        const fourteenDaysMs = 14 * 24 * 60 * 60 * 1000;

        return (
          response.data?.filter((loan) => {
            const retrievedTime = new Date(loan.dateRetrieved).getTime();
            const diff = now - retrievedTime;
            return loan.dateReturned === null && diff > fourteenDaysMs;
          }).length ?? 0
        );
      }),
      catchError(() => of(0)),
    );
  }
}
