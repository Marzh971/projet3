import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ServiceResponse } from '../../../shared/models/service-response.model';
import { AuthorRequestDto } from '../dtos/author-request.dto';
import { AuthorResponseDto } from '../dtos/author-response.dto';

@Injectable({
  providedIn: 'root',
})
export class AuthorService {
  private readonly API = '/api/author';

  private readonly http = inject(HttpClient);

  findAll(): Observable<ServiceResponse<AuthorResponseDto[]>> {
    return this.http.get<ServiceResponse<AuthorResponseDto[]>>(this.API);
  }

  findById(id: string): Observable<ServiceResponse<AuthorResponseDto>> {
    return this.http.get<ServiceResponse<AuthorResponseDto>>(`${this.API}/${id}`);
  }

  create(dto: AuthorRequestDto): Observable<ServiceResponse<AuthorResponseDto>> {
    return this.http.post<ServiceResponse<AuthorResponseDto>>(this.API, dto);
  }

  update(
    id: number,
    author: { firstName: string; lastName: string },
  ): Observable<ServiceResponse<AuthorResponseDto>> {
    return this.http.put<ServiceResponse<AuthorResponseDto>>(`${this.API}/${id}`, author);
  }

  delete(id: string): Observable<ServiceResponse<void>> {
    return this.http.delete<ServiceResponse<void>>(`${this.API}/${id}`);
  }
}
