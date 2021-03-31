import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {ProjectModel} from '../model/project.model';
import {SearchProjectFormModel} from '../model/searchProjectForm.model';
@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  apiUrl = environment.apiUrl;
  constructor(
    private http: HttpClient
  ) { }
  getListProject(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/projects`, { observe: 'response' });
  }

  getOneProject(projectId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/projects/` + projectId, { observe: 'response' });
  }

  getProject(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/projects?size=5`, { observe: 'response' });
  }
  addProject(project: ProjectModel ): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/projects`, project, { observe: 'response' });
  }
  searchProject(searchFrom?: SearchProjectFormModel): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/projects/do-search`, searchFrom, { observe: 'response' });
  }
}
