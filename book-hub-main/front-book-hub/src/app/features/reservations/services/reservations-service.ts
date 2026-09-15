import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ReservationsRequestDto } from '../dtos/reservations-request.dto';
import { ReservationsResponseDto } from '../dtos/reservations-response.dto';
import { ServiceResponse } from '../../../shared/models/service-response.model';

@Injectable({
  providedIn: 'root',
})
export class ReservationsService {
  private readonly API = '/api/reservations';
  private readonly http = inject(HttpClient);

  findAll(): Observable<ServiceResponse<ReservationsResponseDto[]>> {
    return this.http.get<ServiceResponse<ReservationsResponseDto[]>>(this.API);
  }

  findById(id: number): Observable<ServiceResponse<ReservationsResponseDto>> {
    return this.http.get<ServiceResponse<ReservationsResponseDto>>(`${this.API}/${id}`);
  }

  findByUser(userId: string): Observable<ServiceResponse<ReservationsResponseDto[]>> {
    return this.http.get<ServiceResponse<ReservationsResponseDto[]>>(`${this.API}/users/${userId}`);
  }

  findByBook(bookId: string): Observable<ServiceResponse<ReservationsResponseDto[]>> {
    return this.http.get<ServiceResponse<ReservationsResponseDto[]>>(`${this.API}/books/${bookId}`);
  }

  create(bookIsbn: string): Observable<ServiceResponse<ReservationsResponseDto>> {
    return this.http.post<ServiceResponse<ReservationsResponseDto>>(
      this.API,
      { bookIsbn },
      { withCredentials: true },
    );
  }

  update(
    id: number,
    dto: ReservationsRequestDto,
  ): Observable<ServiceResponse<ReservationsResponseDto>> {
    return this.http.put<ServiceResponse<ReservationsResponseDto>>(`${this.API}/${id}`, dto);
  }

  delete(id: number): Observable<ServiceResponse<void>> {
    return this.http.delete<ServiceResponse<void>>(`${this.API}/${id}`);
  }
}
