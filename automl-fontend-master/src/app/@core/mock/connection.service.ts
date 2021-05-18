import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {ConnectionModel} from '../model/connection.model';
@Injectable({
  providedIn: 'root'
})
export class ConnectionService {
  apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient
  ) { }
  getConnections(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/connections`, { observe: 'response' });
  }
  saveConnection(connection: ConnectionModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/connections`, connection, { observe: 'response' })
  }
}
