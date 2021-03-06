import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class HadoopService {
  apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
  ) {
  }

  getFolders(path: string): Observable<any> {

    return this.http.get<any>(`${this.apiUrl}/hdfs?path=/${path}`, {observe: 'response'});
  }
}
