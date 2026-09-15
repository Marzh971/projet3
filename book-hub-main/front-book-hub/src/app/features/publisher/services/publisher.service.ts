import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ServiceResponse } from '../../../shared/models/service-response.model';
import { PublisherRequestDto } from '../dtos/publisher-request.dto';
import { PublisherResponseDto } from '../dtos/publisher-response.dto';

@Injectable({
  providedIn: 'root',
})
export class PublisherService {
  private readonly API = '/api/publisher';

  private readonly http = inject(HttpClient);

  findAll(): Observable<ServiceResponse<PublisherResponseDto[]>> {
    return this.http.get<ServiceResponse<PublisherResponseDto[]>>(this.API);
  }

  findById(id: string): Observable<ServiceResponse<PublisherResponseDto>> {
    return this.http.get<ServiceResponse<PublisherResponseDto>>(`${this.API}/${id}`);
  }

  create(dto: PublisherRequestDto): Observable<ServiceResponse<PublisherResponseDto>> {
    return this.http.post<ServiceResponse<PublisherResponseDto>>(this.API, dto);
  }

  update(id: string, dto: PublisherRequestDto): Observable<ServiceResponse<PublisherResponseDto>> {
    return this.http.put<ServiceResponse<PublisherResponseDto>>(`${this.API}/${id}`, dto);
  }

  delete(id: string): Observable<ServiceResponse<void>> {
    return this.http.delete<ServiceResponse<void>>(`${this.API}/${id}`);
  }
}
