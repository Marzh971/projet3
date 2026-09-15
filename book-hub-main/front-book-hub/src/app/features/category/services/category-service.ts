import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoryResponseDTO } from '../dtos/category-response.dto';
import { ServiceResponse } from '../../../shared/models/service-response.model';
import { CategoryRequestDTO } from '../dtos/category-request.dto';
@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private readonly API = '/api/categories';
  private readonly http = inject(HttpClient);

  findAll(): Observable<ServiceResponse<CategoryResponseDTO[]>> {
    return this.http.get<ServiceResponse<CategoryResponseDTO[]>>(`${this.API}`);
  }
  delete(id: BigInt): Observable<ServiceResponse<void>> {
    return this.http.delete<ServiceResponse<void>>(`${this.API}/${id}`);
  }
  findAllDto(): Observable<ServiceResponse<CategoryResponseDTO[]>> {
    return this.http.get<ServiceResponse<CategoryResponseDTO[]>>(`${this.API}` + `/libelle`);
  }

  update(
    id: bigint,
    category: CategoryResponseDTO,
  ): Observable<ServiceResponse<CategoryResponseDTO>> {
    return this.http.put<ServiceResponse<CategoryResponseDTO>>(`${this.API}/${id}`, category);
  }

  create(dto: CategoryRequestDTO): Observable<ServiceResponse<CategoryResponseDTO>> {
    return this.http.post<ServiceResponse<CategoryResponseDTO>>(`${this.API}`, dto);
  }
}
