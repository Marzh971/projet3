import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserResponseDto } from '../dtos/user-response.dto';
import { ServiceResponse } from '../../../shared/models/service-response.model';
import { UserRequestDto } from '../dtos/user-request.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API = '/api/users';

  private readonly http = inject(HttpClient);

  findAll(): Observable<ServiceResponse<UserResponseDto[]>> {
    return this.http.get<ServiceResponse<UserResponseDto[]>>(`${this.API}`);
  }

  findById(id: string): Observable<ServiceResponse<UserResponseDto>> {
    return this.http.get<ServiceResponse<UserResponseDto>>(`${this.API}/${id}`);
  }


  create(dto: UserRequestDto): Observable<ServiceResponse<UserResponseDto>> {
    return this.http.post<ServiceResponse<UserResponseDto>>(`${this.API}`, dto);
  }

  delete(id: string): Observable<ServiceResponse<void>> {
    return this.http.delete<ServiceResponse<void>>(`${this.API}/${id}`);
  }
}
