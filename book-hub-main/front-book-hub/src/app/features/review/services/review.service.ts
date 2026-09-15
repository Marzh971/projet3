import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ReviewRequestDto } from '../dtos/review-request.dto';
import { ReviewListResponseDto } from '../dtos/review-list-response.dto';
import { ReviewResponse } from '../dtos/review-response.dto';
import { ReviewUpdateRequestDto } from '../dtos/review-update.dto';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = '/api/review';

  findAll(): Observable<{
    code: string;
    message: string;
    data: ReviewListResponseDto[];
  }> {
    return this.http.get<{
      code: string;
      message: string;
      data: ReviewListResponseDto[];
    }>(this.apiUrl);
  }

  create(data: ReviewRequestDto): Observable<{
    code: string;
    message: string;
    data: ReviewResponse;
  }> {
    return this.http.post<{
      code: string;
      message: string;
      data: ReviewResponse;
    }>(this.apiUrl, data);
  }

  update(
    id: string,
    data: ReviewUpdateRequestDto,
  ): Observable<{
    code: string;
    message: string;
    data: ReviewResponse;
  }> {
    return this.http.put<{
      code: string;
      message: string;
      data: ReviewResponse;
    }>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: string): Observable<{
    code: string;
    message: string;
    data: null;
  }> {
    return this.http.delete<{
      code: string;
      message: string;
      data: null;
    }>(`${this.apiUrl}/${id}`);
  }
}
