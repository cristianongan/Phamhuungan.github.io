import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {SearchModel} from '../model/search.model';
import {RunNewModel} from '../model/runNewModel.model';
import {RunExModel} from '../model/runExModel.model';

@Injectable({
  providedIn: 'root'
})
export class ModelDetailService {
  apiUrl = environment.apiUrl;
  apiNotebook = environment.apiNotebook;

  constructor(
    private http: HttpClient
  ) { }
  getModelDetail(modelId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/models/` + modelId, { observe: 'response' });
  }
  runModel(runNewModel: RunNewModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/models/run-new` , runNewModel, { observe: 'response' });
  }
  runExsModel(runExModel: RunExModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/models/run-exist` , runExModel, { observe: 'response' });
  }
  doSearch(modelSearch: SearchModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/models/do-search`, modelSearch, { observe: 'response' });
  }
  doSearchModelType(modelSearch: SearchModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/models/do-search-model-type`, modelSearch, { observe: 'response' });
  }
  getHistoryModel(historyId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/histories/` + historyId, { observe: 'response' });
  }
  getInterpreter(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/histories/interpreter`, { observe: 'response' });
  }
  getLogs(historyId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/notebook-logs/by-history/${historyId}`, { observe: 'response' });
  }
  refreshLog(modelId: number): Observable<any> {
    return this.http.get<any>(`${this.apiNotebook}/notebook/refresh-log/${modelId}`, { observe: 'response' });
  }
}
