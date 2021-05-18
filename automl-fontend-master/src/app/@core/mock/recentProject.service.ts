import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {ConnectionModel} from '../model/connection.model';
@Injectable({
  providedIn: 'root'
})
export class RecentProjectService {
  apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient
  ) { }
  getRecentProject(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/models/recent`, { observe: 'response' });
  }

  getChart(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/histories/percent`, { observe: 'response' });
  }
}
