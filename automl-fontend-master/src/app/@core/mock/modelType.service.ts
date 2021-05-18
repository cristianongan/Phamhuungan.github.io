import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {ConnectionModel} from '../model/connection.model';
import {SearchModel} from '../model/search.model';
@Injectable({
  providedIn: 'root'
})
export class ModelTypeService {
  apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient
  ) { }
  getModelType(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/model-types`, { observe: 'response' });
  }
}
