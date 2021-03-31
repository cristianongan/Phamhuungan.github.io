import {Component, OnInit, TemplateRef} from '@angular/core';
import {NbDialogService, NbToastrService} from '@nebular/theme';
import {ModelDetailComponent} from '../../model-detail/model-detail.component';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ProjectListModel} from '../../../@core/model/projectList-model';
import {formatDate} from '@angular/common';
import * as moment from 'moment';
import {type} from 'os';
import {SearchProjectFormModel} from '../../../@core/model/searchProjectForm.model';
import {ProjectService} from '../../../@core/mock/project.service';
import {InterpreterModel} from '../../../@core/model/interpreter.model';
import {ModelDetailService} from '../../../@core/mock/modelDetail.service';
import {Isearch, SearchModel} from '../../../@core/model/search.model';
import {GetComboboxService} from '../../../@core/mock/getCombobox.service';

@Component({
  selector: 'ngx-monitor-project',
  templateUrl: './monitor-project.component.html',
  styleUrls: ['./monitor-project.component.scss'],
})
export class MonitorProjectComponent implements OnInit {
  columnsProject = [
    {name: 'Project', prop: 'projectName', flex: 1},
    {name: 'User', prop: 'createUser', flex: 1},
    {name: 'Created time', prop: 'createTime', flex: 1},
    {name: 'Number of models used', prop: 'numOfModel', flex: 1},
    {name: '', prop: 'actions', flex: 1},
  ];
  columnsViewProject = [
    {name: 'Model Name', prop: 'modelName', flex: 2},
    {name: 'Type', prop: 'bestModelType', flex: 0.5},
    // {name: 'Task', prop: 'tasks', flex: 1},
    {name: '', prop: 'actions', flex: 0.5},
  ];
  searProjectForm: FormGroup;
  searchMdel: FormGroup;
  projectList: ProjectListModel[];
  project: ProjectListModel;
  searchFrom: SearchProjectFormModel;
  resultModel: Isearch[];
  isLoading: boolean = false;
  modeItem = [
    {name: 'Auto select model', prop: 0},
    {name: 'Manual define model', prop: 1},
  ];
  modelType = [];

  constructor(
    public dialogService: NbDialogService,
    public fb: FormBuilder,
    private projectService: ProjectService,
    private toastrService: NbToastrService,
    private getComboboxService: GetComboboxService,
    private modelDetailService: ModelDetailService) {
  }

  ngOnInit(): void {
    this.searProjectForm = this.fb.group({
      projectName: [null, [Validators.pattern]],
      createUser: [null, [Validators.pattern]],
      createFrom: [null],
      createTo: [null],
    })
    this.searchMdel = this.fb.group({
      modelName: [null, [Validators.pattern]],
      createUser: [null, [Validators.pattern]],
      projectName: [null, [Validators.pattern]],
      bestModelType: [null],
      modelMode: [null],
      modelType: [null],
      train: [null],
      test: [null],
      inference: [null],
      mode: [null],
      type: [null],
      task: [null],
      projectId: [null],
    })
    this.searchProject();
    this.getModelType();
  }

  modelDetail(index: number) {
    this.dialogService.open(ModelDetailComponent, {
      closeOnEsc: true,
      context: {
        modelId: this.resultModel[index].modelId,
      },
    });
  }

  searchProject() {
    this.isLoading = true;
    if (this.searProjectForm.value.projectName === '')
      this.searProjectForm.value.projectName = null;
    if (this.searProjectForm.value.createUser === '')
      this.searProjectForm.value.createUser = null;
    if (this.searProjectForm.value.createTo)
      this.searProjectForm.value.createTo = moment(this.searProjectForm.value.createTo).format('YYYY-MM-DD');
    if (this.searProjectForm.value.createFrom)
      this.searProjectForm.value.createFrom = moment(this.searProjectForm.value.createFrom).format('YYYY-MM-DD');
    this.searchFrom = this.searProjectForm.value;
    this.projectService.searchProject(this.searchFrom).subscribe(
      res => {
        if (res.body.code === '00') {
          this.projectList = res.body.data;
          this.isLoading = false;
        } else {
          this.toastrService.danger(res.body.message, 'Error', {icon: 'alert-triangle-outline'});
          this.isLoading = false;
        }
      }, error => {
        this.toastrService.danger(error.error.message, 'Error', {icon: 'alert-triangle-outline'});
        this.isLoading = false;
      },
    )
  }

  projectDetail(viewProject: TemplateRef<any>, index: number) {
    this.isLoading = true;
    this.projectService.getOneProject(this.projectList[index].projectId).subscribe(
      res => {
        if (res.body.code === '00') {
          this.project = res.body.data;
          this.searchModelOfProject(this.projectList[index].projectId)
          this.dialogService.open(viewProject, {
            context: {projectId: this.projectList[index].projectId},
            hasBackdrop: true
          }).onClose.subscribe(value => {
            this.searchMdel.reset();
          })
          this.isLoading = false;
        } else {
          this.toastrService.danger(res.body.message, 'Error', {icon: 'alert-triangle-outline'});
          this.isLoading = true;
          setTimeout(() => this.isLoading = false, 10000)
        }
      }, error => {
        this.toastrService.danger(error.error.message, 'Error', {icon: 'alert-triangle-outline'});
        this.isLoading = true;
        setTimeout(() => this.isLoading = false, 10000)
      },
    )
  }

  searchModelOfProject(projectId: number) {
    /*let train: string;
    let test: string;
    let inference: string;
    this.searchMdel.value.train === true ? train = '1' : train = '0';
    this.searchMdel.value.test === true ? test = '1' : test = '0';
    this.searchMdel.value.inference === true ? inference = '1' : inference = '0';
    if (this.searchMdel.value.train != null || this.searchMdel.value.test != null || this.searchMdel.value.inference != null) {
      this.searchMdel.value.task = train + '/' + test + '/' + inference;
    }*/
    this.isLoading = true;
    this.searchMdel.value.projectId = projectId;
    this.modelDetailService.doSearch(this.searchMdel.value).subscribe(
      res => {
        if (res.body.code === '00') {
          this.isLoading = false;
          this.resultModel = res.body.data;
        } else {
          this.toastrService.danger(res.body.message, 'Error', {icon: 'alert-triangle-outline'});
          this.isLoading = false;
        }
      }, error => {
        this.toastrService.danger(error.error.message, 'Error', {icon: 'alert-triangle-outline'});
        this.isLoading = false;
      },
    )
  }

  getModelType() {
    this.getComboboxService.getModelType().subscribe(
      res => {
        if (res.body.code === '00') {
          this.modelType = res.body.data
        }
      }
    )
  }
}
