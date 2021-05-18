import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import { ListParameter} from '../model/listParameter.model';
@Injectable({
  providedIn: 'root'
})
export class ParametersService {
  apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient
  ) { }
  getParametersById(parameter: ListParameter): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/parameters/do-search`, parameter, { observe: 'response' });
  }
}
