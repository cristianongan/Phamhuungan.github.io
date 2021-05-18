import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PrimeNGConfig} from 'primeng/api';
import {NbDialogService, NbToastrService} from '@nebular/theme';
import {ConnectionModel, IConnection} from '../../../@core/model/connection.model';
import {ConnectionService} from '../../../@core/mock/connection.service';
import {ProjectService} from '../../../@core/mock/project.service';
import {Iproject, ProjectModel} from '../../../@core/model/project.model';
import {ModelDetailService} from '../../../@core/mock/modelDetail.service';
import {SqlQueryModel} from '../../../@core/model/sqlQuery.model';
import {HiveDbService} from '../../../@core/mock/hiveDb.service';
import {Ifolder} from '../../../@core/model/folder.model';
import {HadoopService} from '../../../@core/mock/hadoop.service';
import {RunExModel} from '../../../@core/model/runExModel.model';
import {GetComboboxService} from '../../../@core/mock/getCombobox.service';
import {ModelTypeService} from '../../../@core/mock/modelType.service';
import {SearchModel} from '../../../@core/model/search.model';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'ngx-run-existing-model',
  templateUrl: './run-existing-model.component.html',
  styleUrls: ['./run-existing-model.component.scss'],
})
export class RunExistingModelComponent implements OnInit {
  @ViewChild('confirmPopup') confirmPopup: TemplateRef<any>;
  isLoading: boolean = false;
  disableSave: boolean = true;
  turningType = [];
  metrics = [];
  result = [];
  informationForm: FormGroup;
  dataSrc: FormGroup;
  thirdForm: FormGroup;
  fifthForm: FormGroup;
  selectModel: FormGroup;
  newConnectionForm: FormGroup;
  year: number = 0;
  scheduleErr: string = '';
  dayOfWeek = '';
  searchModelObj: SearchModel = {};
  checkConn: boolean;
  hideStatus: boolean = false;
  connection: IConnection;
  column = [
    {name: 'id', prop: 'id'},
    {name: 'sqlCommand', prop: 'sqlCommand'},
    {name: 'createCommand CREATE', prop: 'createCommand'},
    {name: 'tableName NAME', prop: 'tableName'},
  ];
  columns = [
    {name: 'Item', prop: 'path', flex: 1},
    {name: 'Size', prop: 'size', flex: 1},
    {name: 'Last Modifier', prop: 'modification_time', flex: 1},
    {name: 'Owner', prop: 'owner', flex: 1},
  ];
  parametersColumn = [
    {name: '', prop: 'stt', flex: 1},
    {name: 'Key', prop: 'key', flex: 1},
    {name: 'Value', prop: 'value', flex: 1},
  ];
  labelColumn: string = '';
  connections: IConnection[];
  projects: Iproject[];
  /*step 2*/

  sqlText: '';
  sqlError: string = '';
  sqlQueryModel: SqlQueryModel = {};
  inferenceTable: [];
  testingTable: [];
  validateTable: [];
  trainingTable: [];
  keyTrainingTable: [];
  keyValidateTable: [];
  keyTestingTable: [];
  keyInferenceTable: [];
  /*step 3*/
  selected = [];
  modelArr = [{name: 'Auto select model', value: 0}, {name: 'Manual define model', value: 1}]
  modelId: number;
  modelTypeArr: [];
  /*step 4*/
  tableColumn = [];
  labelColumns = [];
  tableColumnFeaPro = [];
  tableColumnOutPro = [];
  tableColumnFea = [];
  tableColumnOut = [];
  _raw_prediction: boolean = false;
  _prediction: boolean = false;
  _probability: boolean = false;
  /*step 5*/
  tempfolder: Ifolder[];
  pathResult: string = '';
  pathArr = [];
  modeItem = [];
  outputTableName: string = '';
  isOutputTableName: boolean = false;
  outputTableMode: string = '';
  outputTablePartition: string = '';
  location: string = '';
  arrPrePath = [];
  errorPath: string = '';
  runExModel: RunExModel = {};
  columnModel = [
    {name: '', prop: 'checked', flex: 1},
    {name: 'Model', prop: 'modelName', flex: 1},
    {name: 'Projects', prop: 'projectName', flex: 1},
    {name: 'Created time', prop: 'createTime', flex: 1},
    {name: 'Model type', prop: 'modelTypeName', flex: 1},
    {name: 'Train/Val/Test score', prop: 'modelCore', flex: 1},
  ]

  rowParameter = [];

  constructor(
    private fb: FormBuilder,
    private primengConfig: PrimeNGConfig,
    private toastrService: NbToastrService,
    public dialogService: NbDialogService,
    private connectionService: ConnectionService,
    private projectService: ProjectService,
    private modelDetailService: ModelDetailService,
    private folderService: HadoopService,
    private modelTypeService: ModelTypeService,
    private getComboboxService: GetComboboxService,
    public hiveDbService: HiveDbService,
    private datePipe: DatePipe) {

    this.informationForm = this.fb.group({
      projectId: [null, [Validators.required]],
      runNote: [null, [Validators.maxLength(255), Validators.pattern, Validators.required]],
      runNow: [1],
      schedule: [null],
      test: [false],
      train: [false],
      inference: [false],
      minute: [null],
      hour: [null],
      DOM: [null],
      month: [null],
      DOW: [null],
    });
    this.newConnectionForm = this.fb.group({
        connectionName: [null, [Validators.required, Validators.pattern]],
        connectionUrl: [null],
        driverClassName: [null],
        userName: [null],
        passWord: [null],
      },
    )
    this.dataSrc = this.fb.group({
        connectionId: [null, [Validators.required]],
        parameters: [''],
        trainingTable: [null, [Validators.required]],
        validationTable: [null, [Validators.required]],
        testingTable: [null, [Validators.required]],
        inferenceTable: [null, [Validators.required]],
      },
    )
    this.selectModel = this.fb.group({
      modelName: [null, [Validators.pattern]],
      modelMode: [null],
      modelType: [null],
    });
  }

  ngOnInit() {
    this.year = new Date().getFullYear();
    this.getModelType();
    this.getConnection();
    this.getProject();
    this.primengConfig.ripple = true;
    this.getFolders('/', true);
    this.getCombobox();
    this.getParameterJs();
  }

  getModelType() {
    this.modelTypeService.getModelType().subscribe(
      res => {
        if (res.body.code === '00') {
          this.modelTypeArr = res.body.data;
        }
      },
    )
  }

  getConnection() {
    this.connectionService.getConnections().subscribe(res => {
      // if (res.body.code === '00') {
      this.connections = res.body;
      this.dataSrc.get('connectionId').patchValue(this.connections[0].connectionId);
      // }
    });
  }

  createConnection(ref: any, ref2: any) {
    this.isLoading = true;
    this.connectionService.saveConnection(this.newConnectionForm.value).subscribe(res => {
      this.getConnection();
      ref2.close();
      ref.close();
      this.isLoading = false;
      this.toastrService.success('Successful', 'Notify')
    }, error => {
      this.isLoading = false;
      this.toastrService.success(error.error.error, 'Error')
    });
  }

  getProject() {
    this.projectService.getListProject().subscribe(res => {
      if (res.body.code === '00') {
        this.projects = res.body.data;
        this.informationForm.get('projectId').patchValue(this.projects[0].projectId)
        this.searChModel();
      }
    });
  }

  searChModel() {
    this.isLoading = true;
    this.searchModelObj = this.selectModel.value;
    this.searchModelObj.projectId = this.informationForm.value.projectId;
    this.modelDetailService.doSearchModelType(this.searchModelObj).subscribe(res => {
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

  /*step 1*/
  getDayOfWeek(e) {
    if (e.key === '+') e.preventDefault();
    if (e.key === '-') e.preventDefault();
    if (e.key === 'E') e.preventDefault();
    if (e.key === 'e') e.preventDefault();

    this.dayOfWeek = e.target.value;
  }

  /*step 2*/

  submitSQL(action: string, ref: any) {
    switch (action) {
      case 'trainingTable': {
        this.isLoading = true;
        this.getDataSQL(ref, this.sqlText, action);
        break;
      }
      case 'validationTable': {
        this.isLoading = true;
        this.getDataSQL(ref, this.sqlText, action);
        break;
      }
      case 'testingTable': {
        this.isLoading = true;
        this.getDataSQL(ref, this.sqlText, action);
        break;
      }
      case 'inferenceTable': {
        this.isLoading = true;
        this.getDataSQL(ref, this.sqlText, action);
        break;
      }
    }
  }

  getDataSQL(ref, sqlQuery, action: string) {
    this.isLoading = true;
    let isCanceled = true;
    this.sqlQueryModel.sql = sqlQuery + ' limit 10';
    this.sqlQueryModel.connectionId = this.dataSrc.value.connectionId;
    const paramsObj = {};
    this.rowParameter.forEach(i => {
        paramsObj[i.key] = i.value
      }
    )
    this.sqlQueryModel.params = paramsObj;
    const callApi = this.hiveDbService.dataSql(this.sqlQueryModel).subscribe(
      res => {
        isCanceled = false;
        if (res.body.code === '00') {
          switch (action) {
            case 'trainingTable': {
              this.keyTrainingTable = res.body.data[0];
              this.trainingTable = res.body.data;
              this.dataSrc.get('trainingTable').patchValue(this.sqlText);
              break;
            }
            case 'validationTable': {
              this.keyValidateTable = res.body.data[0];
              this.validateTable = res.body.data;
              this.dataSrc.get('validationTable').patchValue(this.sqlText);
              break;
            }
            case 'testingTable': {
              this.keyTestingTable = res.body.data[0];
              this.testingTable = res.body.data;
              this.dataSrc.get('testingTable').patchValue(this.sqlText);
              break;
            }
            case 'inferenceTable': {
              this.keyInferenceTable = res.body.data[0];
              this.inferenceTable = res.body.data;
              this.dataSrc.get('inferenceTable').patchValue(this.sqlText);
              break;
            }
          }
          this.isLoading = false;
          ref.close();
        } else {
          this.sqlError = 'Format of SQL is wrong';
          this.isLoading = false;
        }
      }, error => {
        this.sqlError = 'Format of SQL is wrong';
        isCanceled = false;
        this.isLoading = false;
      },
    )
    setTimeout(() => {
      if (isCanceled) {
        callApi.unsubscribe();
        this.isLoading = false;
        this.sqlError = 'Request timeout.'
      }
    }, 300000)
  }

  setSqlDefault() {
    if (!this.informationForm.value.train) {
      this.dataSrc.get('trainingTable').patchValue(' ');
      this.dataSrc.get('validationTable').patchValue(' ');
    } else {
      this.dataSrc.get('trainingTable').patchValue('');
      this.dataSrc.get('validationTable').patchValue('');
    }
    if (!this.informationForm.value.test) {
      this.dataSrc.get('testingTable').patchValue(' ');
    } else {
      this.dataSrc.get('testingTable').patchValue('');
    }
    if (!this.informationForm.value.inference) {
      this.dataSrc.get('inferenceTable').patchValue(' ');
    } else {
      this.dataSrc.get('inferenceTable').patchValue('');
    }
  }

  getTableColumn(trainingTable: string, connectionId: number) {
    this.isLoading = true;
    this.hiveDbService.getTableColunm(trainingTable, connectionId).subscribe(
      res => {
        if (res.body.code === '00') {
          this.isLoading = false;
          this.tableColumn = res.body.data;

          this.tableColumnFeaPro = [...this.tableColumn];
          this.tableColumnOutPro = [...this.tableColumn];
          /*
                    this.configFlow.outputColumnArr.forEach(item =>
                      this.tableColumnOut.splice(this.tableColumnOut.indexOf(item), 1),
                    )
                    this.configFlow.featureColumnArr.forEach(item =>
                      this.tableColumnFea.splice(this.tableColumnFea.indexOf(item), 1),
                    )*/

        } else {
          this.isLoading = false;
          this.toastrService.danger(res.body.message, 'Error')
        }
      }, error => {
        this.isLoading = false;
        this.toastrService.danger(error.error.message, 'Error')
      }
    )
  }

  /*step 5*/

  getFolders(path?: string, resetPath?: boolean) {
    this.isLoading = true;
    let searchPath = '';
    if (path) {
      searchPath = path.startsWith('/') ? path.replace('/', '') : path;
    }
    if (resetPath) {
      searchPath ? this.pathArr.push(searchPath) : this.pathArr;
      this.pathResult = this.pathArr.join('/');
    } else {
      this.pathResult = searchPath;
      this.pathArr = this.pathResult ? this.pathResult.split('/') : [];
    }
    this.folderService.getFolders(this.pathResult).subscribe(
      res => {
        if (res.body.code === '00') {
          this.tempfolder = res.body.data;
          this.tempfolder.forEach(i => {
            i.path = i.path.split('/')[i.path.split('/').length - 1];
          })
          if (resetPath) {
            this.arrPrePath = [];
          }
          this.isLoading = false;
          this.disableSave = false;
          this.errorPath = '';
        } else {
          this.errorPath = res.body.message;
          this.isLoading = false;
          this.disableSave = true;
          this.arrPrePath = [];
        }
      }, error => {
        this.errorPath = error.error.message;
        this.isLoading = false;
        this.disableSave = true;
        this.arrPrePath = [];
      }
    )
  }

  onSelect(e?, i?: number) {
    this.modelId = this.result[i].modelId;
  }

  save(ref) {
    let train: string;
    let test: string;
    let inference: string;
    const informationObj = {...this.informationForm.value};
    /*get project*/
    /*end*/
    informationObj.train === true ? train = '1' : train = '0';
    informationObj.test === true ? test = '1' : test = '0';
    informationObj.inference === true ? inference = '1' : inference = '0';
    this.informationForm.value.task = train + ',' + test + ',' + inference;
    if (informationObj.runNow === 0) {
      this.informationForm.value.schedule = informationObj.minute + ' ' + informationObj.hour + ' ' + informationObj.DOM + ' ' + informationObj.month + ' ' + this.dayOfWeek;
    } else {
      this.informationForm.value.schedule = null;
    }
    this.isLoading = true;
    /*sttep 3*/

    const tableColumnFeaArr = [];
    this.tableColumnFea.forEach(i => tableColumnFeaArr.push(i.name));
    const tableColumnFea = tableColumnFeaArr.join(',');
    const outputColumnsArr = [];

    this.tableColumnOut.forEach(i => outputColumnsArr.push(i.name));
    const outputColumns = outputColumnsArr.join(',');


    const _raw_prediction = outputColumns ? ',_raw_prediction' : '';
    const _prediction = outputColumns ? ',_prediction' : '';
    const _probability = outputColumns ? ',_probability' : '';
    /*end*/
    this.runExModel = {
      ...this.informationForm.value, ...this.dataSrc.value,
      featureColumns: tableColumnFea,
      outputColumns: outputColumns,
      modelId: this.modelId,
      labelColumns: this.labelColumn + _raw_prediction + _prediction + _probability,
      location: !this.pathResult ? '/' : this.pathResult,
      outputTableName: this.outputTableName,
      outputTableMode: this.outputTableMode,
      outputTablePartition: this.outputTablePartition,
    }
    this.modelDetailService.runExsModel(this.runExModel).subscribe(
      res => {
        if (res.body.code === '00') {
          this.isLoading = false;
          ref.close();
          this.toastrService.success('Successfully', 'Notify')
        } else {
          this.isLoading = false;
          this.toastrService.danger(res.body.debugMessage, 'Error')
        }
      }, error => {
        this.isLoading = false;
        this.toastrService.danger(error.error.message, 'Error')
      }
    )
  }

  testConnection() {
    this.hiveDbService.testConnection(this.newConnectionForm.value).subscribe(res => {
      if (res.body.code === '00') {
        this.checkConn = res.body.data;
        this.hideStatus = true;
        this.isLoading = false;
      } else {
        this.isLoading = false;
        this.toastrService.danger(res.body.message, 'Error')
      }
    }, error => {
      this.isLoading = false;
      this.toastrService.danger(error.error.message, 'Error')
    });
  }

  prevent(e) {
    if (e.key === '+') e.preventDefault();
    if (e.key === '-') e.preventDefault();
    if (e.key === 'E') e.preventDefault();
    if (e.key === 'e') e.preventDefault();
  }

  backFolder() {
    let pathArr = this.pathResult.split('/');
    pathArr = pathArr.filter(Boolean)
    if (this.errorPath !== '') {
      pathArr.pop()
    } else {
      this.arrPrePath.unshift(pathArr.pop()); /*xoa path o vi tri cuoi cung & them path ay vao mang Pre*/
    }
    this.pathResult = pathArr.join('/');
    this.getFolders(this.pathResult);
  }

  preFolder() {
    let path: string = '';
    path = this.arrPrePath[0];
    this.pathResult = this.pathResult + '/' + path
    this.getFolders(this.pathResult);
    this.arrPrePath.shift();
  }

  getCombobox() {
    this.getComboboxService.getAutoTurning().subscribe(
      res => {
        if (res.body.code === '00') {
          this.turningType = res.body.data;
        }
      }
    )
    this.getComboboxService.getMetrics().subscribe(
      res => {
        if (res.body.code === '00') {
          this.metrics = res.body.data;
        }
      }
    )
    this.getComboboxService.getModelModes().subscribe(
      res => {
        if (res.body.code === '00') {
          this.modeItem = res.body.data;
        }
      }
    )
  }

  getLabelColumn(sqlQuery) {
    this.sqlQueryModel.sql = sqlQuery;
    this.sqlQueryModel.connectionId = this.dataSrc.value.connectionId;
    const paramsObj = {};
    this.rowParameter.forEach(i => {
        paramsObj[i.key] = i.value
      }
    )
    this.sqlQueryModel.params = paramsObj;
    this.hiveDbService.getLabelColumm(this.sqlQueryModel).subscribe(
      res => {
        if (res.body.code === '00') {
          this.labelColumns = res.body.data;
        } else {
          this.toastrService.danger(res.body.message, 'Error')
        }
      }, error => {
        this.toastrService.danger(error.error.message, 'Error')
      })
  }

  /*check Output table is exst*/
  checkOutputTable() {
    this.isLoading = true;
    this.sqlQueryModel.sql = 'select * from ' + this.outputTableName + ' limit 1';
    this.sqlQueryModel.connectionId = this.dataSrc.value.connectionId;
    const paramsObj = {};
    this.rowParameter.forEach(i => {
        paramsObj[i.key] = i.value
      }
    )
    this.sqlQueryModel.params = paramsObj;
    this.hiveDbService.dataSql(this.sqlQueryModel).subscribe(
      res => {
        if (res.body.code === '00') {
          if (res.body.data.length > 0) {
            this.dialogService.open(this.confirmPopup, {
              context: {action: 'saveRun'},
              closeOnEsc: true,
              hasBackdrop: true
            });
            this.isOutputTableName = false;
            this.isLoading = false;
          } else {
            this.isOutputTableName = true;
            this.isLoading = false;
          }
        } else {
          this.isOutputTableName = true;
          this.isLoading = false;
        }
      }, error => {
        this.isOutputTableName = true
        this.isLoading = false;
      }
    )
  }

  clearDataTable(action) {
    switch (action) {
      case 'trainingTable': {
        this.dataSrc.get('trainingTable').reset();
        this.trainingTable = null;
        this.keyTrainingTable = null;
        break;
      }
      case 'validationTable': {
        this.dataSrc.get('validationTable').reset();
        this.validateTable = null;
        this.keyValidateTable = null;
        break;
      }
      case 'testingTable': {
        this.dataSrc.get('testingTable').reset();
        this.testingTable = null;
        this.keyTestingTable = null;
        break;
      }
      case 'inferenceTable': {
        this.dataSrc.get('inferenceTable').reset();
        this.inferenceTable = null;
        this.keyInferenceTable = null;
        break;
      }
    }
  }


  getDataConnection() {
    this.connection = this.connections.find(item => item.connectionId === this.dataSrc.value.connectionId);
    this.newConnectionForm.patchValue(this.connection)
  }


  getParameterJs() {
    const dateNow: Date = new Date();
    const YYYYMMDD = this.getDateStr(dateNow, 'yyyyMMdd');
    const YYYYMM01 = this.getDateStr(dateNow, 'yyyyMM') + '01';
    const YYYYMMdd_MM_sub_1 = this.getDateStr(this.getDatePre(dateNow, 1), 'yyyyMMdd')
    this.rowParameter = [
      {key: 'YYYYMMDD', value: YYYYMMDD},
      {key: 'YYYYMM01', value: YYYYMM01},
      {key: 'YYYYMMdd_MM_sub_1', value: YYYYMMdd_MM_sub_1}
    ]
  }

  getDatePre(date: Date, month: number) {
    const previousMonth = new Date(date);
    previousMonth.setMonth(date.getMonth() - month);
    return previousMonth;
  }

  getDateStr(date: Date, format: string): string {
    return this.datePipe.transform(date, format);
  }
}
