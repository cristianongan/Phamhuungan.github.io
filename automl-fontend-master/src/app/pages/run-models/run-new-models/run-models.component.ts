import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PrimeNGConfig} from 'primeng/api';
import {NbDialogService, NbToastrService} from '@nebular/theme';
import {ConnectionService} from '../../../@core/mock/connection.service';
import {IConnection} from '../../../@core/model/connection.model';
import {ImodelTypeDTOs} from '../../../@core/model/modelTypeDTOs.model';
import {ModelTypeService} from '../../../@core/mock/modelType.service';
import {ParametersService} from '../../../@core/mock/parameters.service';
import {IlistParameter, ListParameter} from '../../../@core/model/listParameter.model';
import {SubModelModel} from '../../../@core/model/subModel.model';
import {ModelDetailService} from '../../../@core/mock/modelDetail.service';
import {ProjectService} from '../../../@core/mock/project.service';
import {ProjectListModel} from '../../../@core/model/projectList-model';
import {HadoopService} from '../../../@core/mock/hadoop.service';
import {Ifolder} from '../../../@core/model/folder.model';
import {
  ConfigFlowDTO,
  ModelDTO,
  ModelTypeDTO,
  ModelTypeDTOParameterDTO,
  ProjectDTO,
  RunNewModel,
  SubModelDTO,
  SubModelDTOParameterDTO,
} from '../../../@core/model/runNewModel.model';
import {HiveDbService} from '../../../@core/mock/hiveDb.service';
import {SqlQueryModel} from '../../../@core/model/sqlQuery.model';
import {GetComboboxService} from '../../../@core/mock/getCombobox.service';
import {DatePipe} from '@angular/common';
import {log} from 'util';
import {Router} from '@angular/router';

@Component({
  selector: 'ngx-run-models',
  templateUrl: './run-models.component.html',
  styleUrls: ['./run-models.component.scss'],
})
export class RunModelsComponent implements OnInit {
  @ViewChild('confirmPopup') confirmPopup: TemplateRef<any>;
  isLoading: boolean = false;
  disableSave: boolean = true;
  isOutputTableName: boolean = false;
  required = '';
  informationForm: FormGroup;
  dataSrcForm: FormGroup;
  thirdForm: FormGroup;
  selectModelForm: FormGroup;
  fifthForm: FormGroup;
  newProjectForm: FormGroup;
  newConnectionForm: FormGroup;
  newModelForm: FormGroup;
  connections: IConnection[];
  connection: IConnection;
  checkConn: boolean;
  hideStatus: boolean = false;
  tableColumn = [];
  modelTypeArr: ImodelTypeDTOs[];
  modelTypeFilter = [];
  modelTypeArrFilted: ImodelTypeDTOs[] = [];
  parameter: ListParameter = new ListParameter();
  parameterArr = [];
  projects: ProjectListModel[] = [];
  arr = [];
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
  uniforms = [
    {name: 'uniform', value: 1},
    {name: 'normal', value: 2}
  ];
  turningType = [];
  metrics = [];
  metricsFilter = [];
  modeItem = [];
  uniqueSubModel: boolean = false;
  uniqueProjects: boolean = false;
  subModel: SubModelModel[] = [];
  activeMenu = null;
  tableColumnFeaPro = [];
  tableColumnOutPro = [];
  tableColumnFea = [];
  tableColumnOut = [];
  /*step 1*/
  arrSub = [];
  projectSlected: ProjectDTO = {};
  runNewModel: RunNewModel = {};
  configFlowDTO: ConfigFlowDTO = {};
  modelDTO: ModelDTO = {};
  parameterDTOS: ModelTypeDTOParameterDTO[] = [];
  year: number = 0;
  /*step 2*/
  sqlText: string = '';
  sqlError: string = '';
  sqlQueryModel: SqlQueryModel = {};
  inferenceTable: [];
  testingTable: [];
  validateTable: [];
  trainingTable: any [];
  keyTrainingTable: [];
  keyValidateTable: [];
  keyTestingTable: [];
  keyInferenceTable: [];
  /*step 3*/
  labelColumn: string = '';
  labelColumns = [];
  _raw_prediction: boolean = true;
  _prediction: boolean = true;
  _probability: boolean = true;
  /*step 4*/
  validateNumberRatio: boolean = false;
  validateNumberSub: boolean = false;
  parameters: ListParameter = {};
  subModelDTO: SubModelDTOParameterDTO[] = [];
  modelTypeDTOSObj: ModelTypeDTO = {};
  subModelName = '';
  paramString = [];
  optimizationAlgorithms = [
    {name: 'Random search', value: 'tpe.rand.suggest'},
    {name: 'Bayesian search', value: 'tpe.suggest'}
  ]
  /*step 5*/
  folder: Ifolder[];
  tempfolder: Ifolder[];
  pathResult: string = ''
  pathArr = [];
  outputTableName: string = '';
  outputTableMode: string = '';
  outputTablePartition: string = '';
  location: string = '';
  arrPrePath = [];
  errorPath: string = '';
  /*step 3*/
  subModelDTOs: SubModelDTO[] = [];
  submodelDTOObj: SubModelDTO = {};
  rowParameter = [];

  constructor(
    private fb: FormBuilder,
    private primengConfig: PrimeNGConfig,
    private toastrService: NbToastrService,
    public dialogService: NbDialogService,
    private connectionService: ConnectionService,
    private modelTypeService: ModelTypeService,
    private parametersService: ParametersService,
    private projectService: ProjectService,
    private folderService: HadoopService,
    private getComboboxService: GetComboboxService,
    public modelDetailService: ModelDetailService,
    public hiveDbService: HiveDbService,
    private datePipe: DatePipe,
    private router: Router) {
  }

  ngOnInit() {
    this.primengConfig.ripple = true;
    this.newModelForm = this.fb.group({
        subModelName: [null, [Validators.required, Validators.maxLength(255), Validators.pattern]],
        modelTypeId: [null, [Validators.required]],
      },
    )
    this.newProjectForm = this.fb.group({
        projectName: [null, [Validators.required, Validators.pattern, Validators.maxLength(255)]],
        description: [null],
        projectId: [null],
      },
    )
    this.newConnectionForm = this.fb.group({
        connectionName: [null, [Validators.required, Validators.pattern]],
        connectionUrl: [null],
        driverClassName: [null],
        userName: [null],
        passWord: [null],
        connectionId: [null],

      },
    )
    this.informationForm = this.fb.group({
        projectId: [null, [Validators.required]],
        projectIndex: [null],
        modelName: [null, [Validators.required, Validators.maxLength(255), Validators.pattern]],
        description: [null, [Validators.pattern, Validators.maxLength(255)]],
        runNote: [null, [Validators.maxLength(255), Validators.pattern, Validators.required]],
        schedule: [null],
        runNow: [1],
        test: [true],
        train: [true],
        inference: [true],
        minute: [null],
        hour: [null],
        DOM: [null],
        month: [null],
        DOW: [null],
      },
    )
    this.dataSrcForm = this.fb.group({
        connectionId: [null, [Validators.required]],
        parameters: [null],
        trainingTable: [null, [Validators.required]],
        validationTable: [null, [Validators.required]],
        testingTable: [null, [Validators.required]],
        inferenceTable: [null],
      },
    )
    this.selectModelForm = this.fb.group({
        algorithmType: [0],
        modelMode: [0],
        uniform: [null],
        paramString: [null],
        metrics: [0, [Validators.required]],
        biggerIsBetter: [null],
        autoTurningType: [1, [Validators.required]],
        checkpointStep: [null, [Validators.required]],
        maxTrialTime: [null, [Validators.required]],
        optimizationAlgorithm: ['tpe.random.suggest', [Validators.required]],
        allowPersist: [null],
        ratio: [null],
        numberOfFolds: [null],
        dataSubsamplingRatio: [null, [Validators.required]],
        parameterName: [null],
        parameterId: [null],
        parameterType: [null],
        min: [null],
        max: [null],
        step: [null],
        multiclass: [false],
      },
    )
    this.getCombobox();
    /*step 1*/
    this.getProjects();
    /*step 2*/
    this.getConnection();
    /*step4*/
    this.getModelType();
    /*step3*/
    // this.getTableColumn();
    /*step5*/
    this.getFolders('/', true);

    this.getParameterJs();
  }

  /*step 2*/
  submitSQL(action: string, ref: any) {
    switch (action) {
      case 'trainingTable': {
        this.isLoading = true;
        this.getDataSQL(ref, this.sqlText, action);
        this.getLabelColumn(this.sqlText);
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
    let isCanceled = true;
    let colunmArr = [];
    const colunmProductArr = [];
    const tableColumnFeaObj: Object = {name: '', type: ''}
    this.isLoading = true;
    this.sqlQueryModel.sql = sqlQuery + ' limit 10';
    this.sqlQueryModel.connectionId = this.dataSrcForm.value.connectionId;
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
          this.isLoading = false;
          ref.close();
          switch (action) {
            case 'trainingTable': {
              this.dataSrcForm.get('trainingTable').patchValue(this.sqlText);
              this.keyTrainingTable = res.body.data[0];
              colunmArr = Object.keys(this.keyTrainingTable);
              colunmArr.forEach(
                i => {
                  tableColumnFeaObj['name'] = i;
                  colunmProductArr.push({...tableColumnFeaObj})
                }
              )
              this.tableColumn = [...colunmProductArr];
              this.tableColumnFeaPro = [...colunmProductArr];
              this.tableColumnOutPro = [...colunmProductArr];
              this.trainingTable = res.body.data;
              break;
            }
            case 'validationTable': {
              this.dataSrcForm.get('validationTable').patchValue(this.sqlText);
              this.keyValidateTable = res.body.data[0];
              this.validateTable = res.body.data;
              break;
            }
            case 'testingTable': {
              this.dataSrcForm.get('testingTable').patchValue(this.sqlText);
              this.keyTestingTable = res.body.data[0];
              this.testingTable = res.body.data;
              break;
            }
            case 'inferenceTable': {
              this.dataSrcForm.get('inferenceTable').patchValue(this.sqlText);
              this.keyInferenceTable = res.body.data[0];
              this.inferenceTable = res.body.data;
              break;
            }
          }
        } else {
          this.sqlError = 'Format of SQL is wrong';
          this.isLoading = false;
        }
      }, error => {
        this.sqlError = 'Format of SQL is wrong';
        this.isLoading = false;
        isCanceled = false;
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

  /*end step 2*/

  checkStep5(ref) {
    const informationObj = {...this.informationForm.value};
    this.isLoading = true;
    this.informationForm.value.schedule = null;
    this.informationForm.value.task = '1,1,0';

    const tableColumnFeaArr = [];
    this.tableColumnFea.forEach(i => tableColumnFeaArr.push(i.name));
    const tableColumnFea = tableColumnFeaArr.join(',');
    const outputColumnsArr = [];

    this.tableColumnOut.forEach(i => outputColumnsArr.push(i.name));
    const outputColumns = outputColumnsArr.join(',');
    /*end*/
    /*step 4*/
    if (this.selectModelForm.value.modelMode === 0) {
      this.parameterDTOS = this.parameterArr;
      this.modelTypeDTOSObj = {parameterDTOS: this.parameterDTOS}
      this.subModelDTOs = null;
    } else {
      this.selectModelForm.value.maxTrialTime = null;
      this.selectModelForm.value.optimizationAlgorithm = null;
      this.selectModelForm.value.checkpointStep = null;
      this.modelTypeDTOSObj = null;
    }
    /*end*/
    this.modelDTO = {...this.informationForm.value, ...this.selectModelForm.value}
    const _raw_prediction = this._raw_prediction ? ',_raw_prediction' : '';
    const _prediction = this._prediction ? ',_prediction' : '';
    const _probability = this._probability ? ',_probability' : '';
    this.configFlowDTO = {
      ...this.dataSrcForm.value, ...this.informationForm.value,
      featureColumns: tableColumnFea,
      outputColumns: outputColumns + _raw_prediction + _prediction + _probability,
      outputTableName: this.outputTableName,
      outputTableMode: this.outputTableMode,
      outputTablePartition: this.outputTablePartition,
      labelColumn: this.labelColumn,
      location: !this.pathResult ? '/' : this.pathResult,
    }
    this.projectSlected = this.projects[informationObj.projectIndex]
    this.runNewModel = {
      ...this.runNewModel,
      projectDTO: this.projectSlected,
      modelTypeDTOS: this.modelTypeDTOSObj,
      modelDTO: this.modelDTO,
      subModelDTOS: this.subModelDTOs,
      configFlowDTO: this.configFlowDTO,
      connectionId: this.dataSrcForm.value.connectionId,
    };
    this.modelDetailService.runModel(this.runNewModel).subscribe(
      res => {
        if (res.body.code === '00') {
          ref.close();
          this.toastrService.success('Successfully', 'Notify')
          this.router.navigate(['pages/model-list']);
          this.isLoading = false;
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

  getModelType() {
    this.isLoading = true;
    this.selectModelForm.get('metrics').patchValue(null);
    /*reset modelTYpe*/
    this.modelTypeFilter = [];
    this.modelTypeArrFilted = [];
    this.parameterArr = [];
    /*reset submodel*/
    this.arrSub = [];
    this.subModelDTO = [];
    this.subModel = [];
    this.arr = [];
    this.subModelName = '';
    this.modelTypeService.getModelType().subscribe(
      res => {
        this.isLoading = false;
        if (res.body.code === '00') {
          res.body.data.forEach(i => {
            /*filter model type with algorithmType*/
            if (i.algorithmType === this.selectModelForm.value.algorithmType) {
              this.modelTypeFilter.push(i);
              this.modelTypeArr = [...this.modelTypeFilter]
            }
          })
        }
      },
    )

    /*filter mectrics with algorithmType*/
    this.metrics = [];
    const metricsArr = [];
    this.getComboboxService.getMetrics().subscribe(
      res => {
        if (res.body.code === '00') {
          this.metricsFilter = res.body.data;
          this.metricsFilter.forEach(i => {
            if (i.algorithmType === this.selectModelForm.value.algorithmType)
              metricsArr.push(i);
            this.metrics = [...metricsArr];
          });
        }
      }
    )
  }

  changeCheckbox(e, index) {
    this.isLoading = true;
    this.modelTypeArr[index]['checked'] = e.target.checked;
    let i;
    if (e.target.checked) {
      this.modelTypeArrFilted.push(this.modelTypeArr[index]);
    } else {
      i = this.modelTypeArrFilted.findIndex(value => value.modelTypeId === this.modelTypeArr[index].modelTypeId);
      this.modelTypeArrFilted = this.modelTypeArrFilted.filter(model => model.modelTypeId !== this.modelTypeArr[index].modelTypeId);
    }
    this.parameter.modelTypeId = this.modelTypeArr[index].modelTypeId;
    if (e.target.checked) {
      this.parametersService.getParametersById(this.parameter).subscribe(
        res => {
          this.isLoading = false;
          if (res.body.code === '00') {
            // this.parameterArr.push(res.body.data);
            let ahihi = [];
            const ahuhu = [];
            ahihi = res.body.data;
            ahihi.forEach(item => {
                switch (item.parameterName) {
                  case 'featureSubsetStrategy' : {
                    item.parameterValue = 'auto';
                    this.selectModelForm.get('paramString').patchValue([1])
                    ahuhu.push(item)
                    break;
                  }
                  case 'numTrees' : {
                    item.min = 16,
                      item.max = 128,
                      item.uniform = 1,
                      this.selectModelForm.get('uniform').patchValue(1)
                    item.step = 8
                    ahuhu.push(item)
                    break;
                  }
                  case 'minInstancesPerNode' : {
                    item.min = 50,
                      item.max = 2000,
                      item.uniform = 1,
                      this.selectModelForm.get('uniform').patchValue(1)
                    item.step = 50
                    ahuhu.push(item)
                    break;
                  }
                  case 'minInfoGain' : {
                    item.min = 0.0000001
                    item.max = 0.000001
                    item.uniform = 1
                    this.selectModelForm.get('uniform').patchValue(1)
                    ahuhu.push(item)
                    break;
                  }
                  case 'maxDepth' : {
                    item.min = 10,
                      item.max = 30,
                      item.uniform = 1,
                      this.selectModelForm.get('uniform').patchValue(1)
                    item.step = 2
                    ahuhu.push(item)
                    break;
                  }
                  case 'maxBins' : {
                    item.min = 16,
                      item.max = 128,
                      item.uniform = 1,
                      this.selectModelForm.get('uniform').patchValue(1)
                    item.step = 8
                    ahuhu.push(item)
                    break;
                  }
                  case 'subsamplingRate' : {
                    item.min = 0.5,
                      item.max = 1,
                      item.uniform = 1
                    this.selectModelForm.get('uniform').patchValue(1)
                    ahuhu.push(item)
                    break;
                  }
                  default :
                    ahuhu.push(item)
                    this.selectModelForm.get('uniform').patchValue(null)
                    this.selectModelForm.get('paramString').patchValue(null)
                }
              }
            )
            this.parameterArr.push([...ahuhu]);
          }
        }, error => {
          this.isLoading = false
          this.toastrService.danger(error.error.message, 'Error')
        }
      );
    } else {
      this.parameterArr.splice(i, 1);
      this.isLoading = false;
    }
  }

  changeMin(e, i, paraI) {
    this.parameterArr[i][paraI].min = +e.target.value;
  }

  changeMax(e, i, paraI) {
    this.parameterArr[i][paraI].max = +e.target.value;
  }

  changeUniforms(e, i, paraI) {
    this.parameterArr[i][paraI].uniform = +e.value;
  }

  changeStep(e, i, paraI) {
    this.parameterArr[i][paraI].step = +e.target.value;
  }

  changeValueString(e, i, paraI) {
    const arrValue = [];
    e.forEach(item => {
      arrValue.push(item.value);
    })
    this.parameterArr[i][paraI].parameterValue = arrValue.join(',')
  }

  getProjects() {
    const project: ProjectListModel = {};
    this.projectService.searchProject(project).subscribe(
      res => {
        if (res.body.code === '00') {
          this.projects = res.body.data;
          this.informationForm.get('projectId').patchValue(this.projects[0].projectId)
        }
      },
    )
  }

  getConnection() {
    this.connectionService.getConnections().subscribe(res => {
      // if (res.code.equal('00')) {
      this.connections = res.body;
      this.dataSrcForm.get('connectionId').patchValue(this.connections[0].connectionId);
      this.newConnectionForm.get('connectionId').patchValue(this.connections[0].connectionId)
      this.newConnectionForm.get('connectionName').patchValue(this.connections[0].connectionName)
      this.newConnectionForm.get('connectionUrl').patchValue(this.connections[0].connectionUrl)
      this.newConnectionForm.get('driverClassName').patchValue(this.connections[0].driverClassName)
      this.newConnectionForm.get('userName').patchValue(this.connections[0].userName)
      this.newConnectionForm.get('passWord').patchValue(this.connections[0].passWord)
      // }
    });
  }

  createConnection(ref: any, refConnection) {
    this.isLoading = true;
    this.connectionService.saveConnection(this.newConnectionForm.value).subscribe(res => {
      this.isLoading = false;
      this.getConnection();
      this.toastrService.success('Success', 'Notify')
      ref.close();
      refConnection.close();
    }, error => {
      this.isLoading = false;
      this.toastrService.danger(error.error.message, 'Error')
    });
  }

  testConnection() {
    this.isLoading = true;
    this.hiveDbService.testConnection(this.newConnectionForm.value).subscribe(res => {
      if (res.body.code === '00') {
        this.checkConn = res.body.data;
        this.hideStatus = true;
        this.isLoading = false;
      }
    });
  }

  saveSubModel(ref: any) {
    this.subModel.push(this.newModelForm.value);
    ref.close();
  }

  checkUnique(e) {
    this.uniqueSubModel = this.subModel.some(model => model.subModelName.trim() === e.target.value.trim());
  }

  getTableColumn(trainingTable: string, connectionId: number) {
    this.hiveDbService.getTableColunm(trainingTable, connectionId).subscribe(
      res => {
        if (res.body.code === '00') {
          this.tableColumn = res.body.data;
          this.tableColumnFeaPro = [...this.tableColumn];
          this.tableColumnOutPro = [...this.tableColumn];
        }
      },
    )
  }

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

  addProject(ref: any, ref2: any) {
    this.isLoading = true;
    // this.projects = [...this.projects, this.newProjectForm.value];
    this.projectService.addProject(this.newProjectForm.value).subscribe(
      res => {
        // if (res.body.code === '00') {
        ref.close();
        ref2.close();
        this.getProjects();
        this.isLoading = false;
        // }
      }, error => {
        this.isLoading = false;
      }
    )
  }

  prevent(e) {
    if (e.key === '+') e.preventDefault();
    if (e.key === '-') e.preventDefault();
    if (e.key === 'E') e.preventDefault();
    if (e.key === 'e') e.preventDefault();
  }

  uniqueProject(e) {
    this.uniqueProjects = this.projects.some(model => model.projectName.trim() === e.target.value.trim());
  }

  getParam(i: number) {
    this.isLoading = true;
    this.subModelDTO = this.arrSub[i];
    this.activeMenu = i;
    this.parameters.modelTypeId = this.subModel[i].modelTypeId;
    this.subModelName = this.subModel[i].subModelName;
    if (this.arr.indexOf(i) === -1) {
      this.parametersService.getParametersById(this.parameters).subscribe(
        res => {
          this.isLoading = false;
          if (res.body.code === '00') {
            this.arr.push(i)
            this.subModelDTO = res.body.data;
            this.arrSub.push(this.subModelDTO);
            const ahuhu = [];
            this.subModelDTO.forEach(
              item => {
                switch (item.parameterName) {
                  case 'featureSubsetStrategy' : {
                    item.parameterValue = 'auto';
                    ahuhu.push(item)
                    break;
                  }
                  case 'numTrees' : {
                    item.parameterValue = '32';
                    ahuhu.push(item)
                    break;
                  }
                  case 'minInstancesPerNode' : {
                    item.parameterValue = '500';
                    ahuhu.push(item)
                    break;
                  }
                  case 'minInfoGain' : {
                    item.parameterValue = '0.0001';
                    this.selectModelForm.get('uniform').patchValue(1)
                    ahuhu.push(item)
                    break;
                  }
                  case 'maxDepth' : {
                    item.parameterValue = '30';
                    ahuhu.push(item)
                    break;
                  }
                  case 'maxBins' : {
                    item.parameterValue = '32';
                    ahuhu.push(item)
                    break;
                  }
                  case 'subsamplingRate' : {
                    item.parameterValue = '0.7';
                    ahuhu.push(item)
                    break;
                  }
                  default :
                    ahuhu.push(item)
                    item.parameterValue = null;
                }
              }
            )

            this.submodelDTOObj = {
              parameterDTOS: ahuhu,
              subModelName: this.subModelName,
              modelTypeId: this.subModel[i].modelTypeId,
            }
            this.subModelDTOs.push(this.submodelDTOObj);
          }
        }, error => {
          this.isLoading = false
          this.toastrService.danger(error.error.message, 'Error')
        }
      )
    } else {
      this.isLoading = false;
    }
  }

  getValue(e: any, index) {
    this.subModelDTO[index].parameterValue = e.target.value;
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

  changeFolder() {
  }

  getCombobox() {
    this.getComboboxService.getDataType().subscribe(
      res => {
        if (res.body.code === '00') {
          this.paramString = res.body.data;
        }
      }
    )
    this.getComboboxService.getAutoTurning().subscribe(
      res => {
        if (res.body.code === '00') {
          this.turningType = res.body.data;
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

  nexstep() {
    this.selectModelForm.get('checkpointStep').patchValue(0);
    this.selectModelForm.get('maxTrialTime').patchValue(0);
    this.selectModelForm.get('optimizationAlgorithm').patchValue(0);
  }

  resetMode() {
    this.selectModelForm.get('checkpointStep').patchValue(null);
    this.selectModelForm.get('maxTrialTime').patchValue(null);
    this.selectModelForm.get('optimizationAlgorithm').patchValue(null);
  }

  getLabelColumn(sqlQuery) {
    this.sqlQueryModel.sql = sqlQuery + ' limit 10';
    this.sqlQueryModel.connectionId = this.dataSrcForm.value.connectionId;
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

  checkMaxMinNumber(e, action) {
    if (action === 'ratio') {
      if (e.target.value > 1 || e.target.value < 0) {
        this.validateNumberRatio = true;
      } else {
        this.validateNumberRatio = false;
      }
    } else {
      if (e.target.value > 1 || e.target.value < 0) {
        this.validateNumberSub = true;
      } else {
        this.validateNumberSub = false;
      }
    }
  }

  checkNumber(e?, isDot?) {
    if (e.key === '-' || e.key === '+' || e.key === 'E' || e.key === 'e') {
      e.preventDefault();
    }
    if (isDot) {
      e.key === '.' ? e.preventDefault() : '';
    }
  }

  /*check Output table is exst*/
  checkOutputTable() {
    this.isLoading = true;
    this.sqlQueryModel.sql = 'select * from ' + this.outputTableName;
    this.sqlQueryModel.connectionId = this.dataSrcForm.value.connectionId;
    // this.sqlQueryModel.params = {'partition': 6};
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
        this.dataSrcForm.get('trainingTable').reset();
        this.trainingTable = null;
        this.keyTrainingTable = null;
        break;
      }
      case 'validationTable': {
        this.dataSrcForm.get('validationTable').reset();
        this.validateTable = null;
        this.keyValidateTable = null;
        break;
      }
      case 'testingTable': {
        this.dataSrcForm.get('testingTable').reset();
        this.testingTable = null;
        this.keyTestingTable = null;
        break;
      }
    }
  }

  getDataConnection() {
    this.connection = this.connections.find(item => item.connectionId === this.dataSrcForm.value.connectionId);
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
