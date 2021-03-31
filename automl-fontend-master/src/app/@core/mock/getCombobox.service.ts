import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class GetComboboxService {
  apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
  ) {
  }
  getMetrics(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/metrics`, {observe: 'response'});
  }
  getAutoTurning(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/auto-turning-types`, {observe: 'response'});
  }
  getModelModes(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/model-modes`, {observe: 'response'});
  }
  getDataType(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/data-types`, {observe: 'response'});
  }
  getModelType(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/model-types`, {observe: 'response'});
  }

}
