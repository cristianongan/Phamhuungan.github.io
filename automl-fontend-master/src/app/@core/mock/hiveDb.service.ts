import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {SqlQueryModel} from '../model/sqlQuery.model';
import {ConnectionModel} from '../model/connection.model';

@Injectable({
  providedIn: 'root',
})
export class HiveDbService {
  apiUrl = environment.apiUrl;
  constructor(
    private http: HttpClient,
  ) {
  }

  dataSql(sqlQueryModel: SqlQueryModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/hive-db/execute-sql`, sqlQueryModel, {observe: 'response'});
  }
  getColumn(sqlQueryModel: SqlQueryModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/hive-db/sql-columns`, sqlQueryModel, {observe: 'response'});
  }

  testConnection(connection: ConnectionModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/hive-db/test-conn`, connection, { observe: 'response' })
  }

  getTableColunm(table: string, connectionId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/hive-db/table-columns?table=${table}&connectionId=${connectionId}`, { observe: 'response'});
  }

  getLabelColumm(sqlQueryModel: SqlQueryModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/hive-db/sql-columns`, sqlQueryModel, { observe: 'response'});
  }

}
