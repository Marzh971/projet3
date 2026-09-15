import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServiceResponse } from '../../../shared/models/service-response.model';
import { BookListResponseDto } from '../dtos/book-list-response.dto';
import { BookResponseDto } from '../dtos/book-response.dto';
import { RestPage } from '../../../shared/models/rest-page.model';
import { BookRequestDto } from '../dtos/book-request.dto';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private readonly API = '/api/book';

  private readonly http = inject(HttpClient);

  create(dto: BookRequestDto): Observable<ServiceResponse<BookResponseDto>> {
    return this.http.post<ServiceResponse<BookResponseDto>>(`${this.API}` + `/withCopy`, dto);
  }

  getByIsbn(isbn: string): Observable<ServiceResponse<BookResponseDto>> {
    return this.http.get<ServiceResponse<BookResponseDto>>(`${this.API}/${isbn}`);
  }

  update(isbn: string, dto: BookRequestDto): Observable<ServiceResponse<BookResponseDto>> {
    return this.http.put<ServiceResponse<BookResponseDto>>(`${this.API}/${isbn}`, dto);
  }

  findAll(): Observable<ServiceResponse<BookListResponseDto[]>> {
    return this.http.get<ServiceResponse<BookListResponseDto[]>>(`${this.API}` + `/active`);
  }

  getAllFilter(
    text?: string,
    categoryId?: number,
    page: number = 0,
    size: number = 5,
  ): Observable<ServiceResponse<RestPage<BookListResponseDto>>> {
    let params = new HttpParams().set('page', page).set('size', size);

    if (text) {
      params = params.set('text', text);
    }

    if (categoryId) {
      params = params.set('categoryId', categoryId);
    }
    return this.http.get<ServiceResponse<RestPage<BookListResponseDto>>>(`${this.API}/search`, {
      params,
    });
  }

  setInactive(id: string): Observable<ServiceResponse<void>> {
    return this.http.put<ServiceResponse<void>>(`${this.API}/${id}/inactive`, null);
  }
}
