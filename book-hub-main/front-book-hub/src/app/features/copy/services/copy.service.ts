import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceResponse } from '../../../shared/models/service-response.model';
import { CopyResponseDto } from '../dtos/copy-response.dto';
import { Observable } from 'rxjs';
import { CopyListResponseDto } from '../dtos/copy-list-response.dto';
import { CopyRequestDto } from '../dtos/copy-request.dto';

@Injectable({
  providedIn: 'root',
})
export class CopyService {
  private readonly API = '/api/copies';

  private readonly http = inject(HttpClient);

  public findAll(): Observable<ServiceResponse<CopyListResponseDto[]>> {
    return this.http.get<ServiceResponse<CopyListResponseDto[]>>(this.API);
  }

  public findById(id: number): Observable<ServiceResponse<CopyResponseDto>> {
    return this.http.get<ServiceResponse<CopyResponseDto>>(`${this.API}/${id}`);
  }

  public findByBarcode(barCode: string): Observable<ServiceResponse<CopyResponseDto>> {
    return this.http.get<ServiceResponse<CopyResponseDto>>(`${this.API}/barcode/${barCode}`);
  }

  public save(copy: CopyRequestDto): Observable<ServiceResponse<CopyResponseDto>> {
    return this.http.post<ServiceResponse<CopyResponseDto>>(this.API, copy);
  }

  public delete(id: number): Observable<ServiceResponse<CopyResponseDto>> {
    return this.http.delete<ServiceResponse<CopyResponseDto>>(`${this.API}/${id}`);
  }
}
