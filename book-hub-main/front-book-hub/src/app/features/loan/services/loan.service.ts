import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ServiceResponse } from '../../../shared/models/service-response.model';
import { LoanRequestDto } from '../dtos/loan-request.dto';
import { LoanResponseDto } from '../dtos/loan-response.dto';

@Injectable({
  providedIn: 'root',
})
export class LoanService {
  private readonly API = '/api/loans';

  private readonly http = inject(HttpClient);

  findAll(): Observable<ServiceResponse<LoanResponseDto[]>> {
    return this.http.get<ServiceResponse<LoanResponseDto[]>>(this.API);
  }

  findById(id: string): Observable<ServiceResponse<LoanResponseDto>> {
    return this.http.get<ServiceResponse<LoanResponseDto>>(`${this.API}/${id}`);
  }

  create(dto: LoanRequestDto): Observable<ServiceResponse<LoanResponseDto>> {
    return this.http.post<ServiceResponse<LoanResponseDto>>(this.API, dto);
  }

  update(dto: LoanRequestDto): Observable<ServiceResponse<LoanResponseDto>> {
    return this.http.put<ServiceResponse<LoanResponseDto>>(this.API, dto);
  }

  delete(id: string): Observable<ServiceResponse<void>> {
    return this.http.delete<ServiceResponse<void>>(`${this.API}/${id}`);
  }
}
