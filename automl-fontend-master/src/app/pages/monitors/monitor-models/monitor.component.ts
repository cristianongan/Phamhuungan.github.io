import {Component, OnInit} from '@angular/core';
import {NbDialogService, NbToastrService} from '@nebular/theme';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ModelDetailComponent} from '../../model-detail/model-detail.component';
import {ModelDetailService} from '../../../@core/mock/modelDetail.service';
import {Isearch, SearchModel} from '../../../@core/model/search.model';
import {InterpreterModel} from '../../../@core/model/interpreter.model';
import {GetComboboxService} from '../../../@core/mock/getCombobox.service';

@Component({
  selector: 'ngx-monitor',
  templateUrl: './monitor.component.html',
  styleUrls: ['./monitor.component.scss'],
})
export class MonitorComponent implements OnInit {
  modelList: FormGroup;
  isLoading: boolean = false;
  columns = [
    {name: 'Model Name', prop: 'modelName', flex: 1},
    {name: 'User', prop: 'createUser', flex: 1},
    {name: 'Project', prop: 'projectName', flex: 1},
    // {name: 'Task', prop: 'tasks', flex: 1},
    {name: '', prop: 'actions', flex: 1},
  ];
  responsiveOptions = [
    {
      breakpoint: '1024px',
      numVisible: 3,
      numScroll: 3,
    },
    {
      breakpoint: '768px',
      numVisible: 2,
      numScroll: 2,
    },
    {
      breakpoint: '560px',
      numVisible: 1,
      numScroll: 1,
    },
  ];
  modeItem = [
    {name: 'Auto select model', prop: 0},
    {name: 'Manual define model', prop: 1},
  ];
  modelType = [];
  result: Isearch[];
  interpreterArr: InterpreterModel[];

  constructor(
    private fb: FormBuilder,
    public dialogService: NbDialogService,
    private toastrService: NbToastrService,
    private getComboboxService: GetComboboxService,
    private modelDetailService: ModelDetailService) {
  }

  ngOnInit(): void {
    this.modelList = this.fb.group({
      modelName: [null, [Validators.pattern]],
      createUser: [null, [Validators.pattern]],
      projectName: [null, [Validators.pattern]],
      bestModelType: [null],
      modelMode: [null],
      train: [false],
      test: [false],
      inference: [false],
      mode: [null],
      type: [null],
      task: [null],
      modelType: [null],
    })
    this.getInterpreter();
    this.modelList.value.task = null;
    this.search(true);
    this.getModelType();
  }

  statusColor(status: number): string {
    let color: string;
    if (status === 0)
      color = '#ffaa00';
    if (status === 1)
      color = '';
    if (status === 2)
      color = '#3366ff';
    if (status === 3)
      color = '#00d68f';
    if (status === 4)
      color = '#db2c66';
    return color;
  }

  converTask(task: string): string {
    let tasks = '';
    const arrTask: string[] = task.split('/')
    const train = arrTask[0] === '1' ? 'Train' : '';
    const test = arrTask[1] === '1' ? '   Test' : '';
    const inference = arrTask[2] === '1' ? '   Inference' : '';
    tasks = train + test + inference;
    return tasks;
  }

  convertStatus(status: number): string {
    let statusStr: string;
    switch (status) {
      case 0: {
        statusStr = 'ready';
        break;
      }
      case 1: {
        statusStr = 'running';
        break;
      }
      case 2: {
        statusStr = 'pending';
        break;
      }
      case 3: {
        statusStr = 'done';
        break;
      }
      case 4: {
        statusStr = 'cancel';
        break;
      }
    }
    return statusStr;
  }

  search(raskNull?: boolean) {
    this.isLoading = true;
    this.modelDetailService.doSearch(this.modelList.value).subscribe(res => {
        if (res.body.code === '00') {
          this.result = res.body.data;
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

  modelDetail(index?) {
    this.dialogService.open(ModelDetailComponent, {
      closeOnEsc: true,
      context: {
        modelId: this.result[index].modelId,
      },
    });
  }

  getInterpreter() {
    this.isLoading = true;
    this.modelDetailService.getInterpreter().subscribe(
      res => {
        if (res.body.code === '00') {
          this.interpreterArr = res.body.data;
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
