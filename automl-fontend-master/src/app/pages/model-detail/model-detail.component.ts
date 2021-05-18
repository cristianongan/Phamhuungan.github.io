import {Component, OnInit, TemplateRef} from '@angular/core';
import {NbDialogService, NbToastrService} from '@nebular/theme';
import {ModelDetailService} from '../../@core/mock/modelDetail.service';
import {ModelDetailModel} from '../../@core/model/modelDetail.model';
import {IconfigFlow} from '../../@core/model/configFlow.model';
import {Iparameter} from '../../@core/model/parameter.model';
import {HiveDbService} from '../../@core/mock/hiveDb.service';
import {SqlQueryModel} from '../../@core/model/sqlQuery.model';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'ngx-model-detail',
  templateUrl: './model-detail.component.html',
  styleUrls: ['./model-detail.component.scss'],
})
export class ModelDetailComponent implements OnInit {
  modelId: number = 0;
  tableColumn = [];
  keyTableColumn = [];
  tableColumnFea = [];
  tableColumnOut = [];
  targetColumnOut = [];
  targetColumnFea = [];
  isLoading: boolean = false;
  modelDTO: Object = {};
  metric = '';
  configFlow: IconfigFlow;
  parameters: string = '';
  columnsDetail = [
    {name: 'Run note', prop: 'runNote', flex: 1.5},
    // {name: 'Train/Val/Test Score', prop: 'modelScore', flex: 1.5},
    {name: 'Current status', prop: 'currentStatus', flex: 1},
    {name: 'Task', prop: 'tasks', flex: 1},
    {name: 'Detail', prop: 'detail', flex: 1.5},
    {name: 'Log', prop: 'log', flex: 1.5},
    // {name: 'Infer table', prop: 'inferTable', flex: 1},
    // {name: 'Export', prop: 'export', flex: 1},
  ];
  trainingTable: string;
  connectionName = '';
  connectionId: number;
  modelDetail: ModelDetailModel;
  sqlQueryModel: SqlQueryModel = {};
  logColumn = [
    {name: 'Notebook id', prop: 'notebookId', flex: 1},
    {name: 'Paragraph id', prop: 'paragraphId', flex: 1},
    {name: 'Status', prop: 'status', flex: 0.5},
    {name: 'Start time', prop: 'startTime', flex: 1.5},
    {name: 'End time', prop: 'endTime', flex: 1.5},
    {name: 'Execute Time', prop: 'executeTime', flex: 1}
  ];
  logData = [];
  log = [];
  sqlError: string = '';
  dataSql: [];
  keyDataSql: [];
  searchSpaces: [];
  runType: number;
  modelMode: number;
  raw_prediction: boolean = false;
  prediction: boolean = false;
  probability: boolean = false;
  rowParameter = [];
  modelsId: number = 0;
  constructor(
    private toastrService: NbToastrService,
    public dialogService: NbDialogService,
    public modelDetailService: ModelDetailService,
    public hiveDbService: HiveDbService,
    private datePipe: DatePipe) {
  }

  ngOnInit(): void {
    this.getDetail();
    this.getParameterJs();
  }

  getDetail() {
    this.isLoading = true;
    this.modelDetailService.getModelDetail(this.modelId).subscribe(
      res => {
        if (res.body.code === '00') {
          this.isLoading = false;
          this.modelDetail = res.body.data;
          this.trainingTable = res.body.data.configFlowDTO.trainingTable;
        } else {
          this.isLoading = true;
          setTimeout(() => this.isLoading = false, 10000)
          this.toastrService.danger(res.error.message, 'Error', {icon: 'alert-triangle-outline'});
        }
      }, error => {
        this.isLoading = true;
        setTimeout(() => this.isLoading = false, 10000)
        this.toastrService.danger(error.error.message, 'Error', {icon: 'alert-triangle-outline'});
      },
    )
  }

  getModelHistory(index: number, modelHistory: TemplateRef<any>) {
    this.prediction = false;
    this.raw_prediction = false;
    this.probability = false;
    const tableColumnFeaObj = {name: '', type: ''}
    const tableColumnOutputObj = {name: '', type: ''}
    const columnsOutputObj = {name: '', type: ''}
    const columnsFeatureObj = {name: '', type: ''}
    let columnsOutput = [];
    let columnsFeature = [];
    let outputColumnArr = [];
    this.isLoading = true;
    const historyId: number = this.modelDetail?.historyDTOS[index].historyId;
    this.modelDetailService.getHistoryModel(historyId).subscribe(
      res => {
        if (res.body.code === '00') {
          this.isLoading = false;
          this.modelDTO = res.body.data.modelDTO;
          if (this.modelDTO['metrics']) {
            switch (this.modelDTO['metrics']) {
              case 1:
                this.metric = 'areaUnderROC';
                break;
              case 2:
                this.metric = 'areaUnderPR';
                break;
              case 3:
                this.metric = 'accuracy';
                break;
              case 4:
                this.metric = 'precision';
                break;
              case 5:
                this.metric = 'recall';
                break;
              case 6:
                this.metric = 'f1';
                break;
              case 7:
                this.metric = 'falsePositiveRate';
                break;
              case 8:
                this.metric = 'truePositiveRate';
                break;
              case 9:
                this.metric = 'mae';
                break;
              case 10:
                this.metric = 'mse';
                break;
              case 11:
                this.metric = 'rmse';
                break;
              case 12:
                this.metric = 'r2';
                break;
              default: this.metric = '';
            }
          }
          this.connectionName = res.body.data.connectionDTO.connectionName;
          this.connectionId = res.body.data.connectionDTO.connectionId;
          this.searchSpaces = res.body.data.searchSpace;
          this.configFlow = res.body.data.configFlowDTO;
          outputColumnArr = this.configFlow?.outputColumnArr;
          columnsFeature = res.body.data?.columnsFeature;
          columnsOutput = res.body.data?.columnsOutput;
          const raw = outputColumnArr.indexOf('_raw_prediction');
          if (raw > -1) {
            this.raw_prediction = true;
            outputColumnArr.splice(raw, 1)
          }
          const pre = outputColumnArr.indexOf('_prediction');
          if (pre > -1) {
            this.prediction = true;
            outputColumnArr.splice(pre, 1)
          }
          const pro = outputColumnArr.indexOf('_probability');
          if (pro > -1) {
            this.probability = true;
            outputColumnArr.splice(pro, 1)
          }
          columnsFeature?.forEach(i => {
            columnsFeatureObj['name'] = i;
            this.tableColumnFea.push({...columnsFeatureObj})
          })

          columnsOutput?.forEach(i => {
            columnsOutputObj['name'] = i;
            this.tableColumnOut.push({...columnsOutputObj})
          })

          this.configFlow?.featureColumnArr.forEach(i => {
            tableColumnFeaObj['name'] = i;
            this.targetColumnFea.push({...tableColumnFeaObj})
          })
          outputColumnArr.forEach(i => {
            tableColumnOutputObj['name'] = i;
            this.targetColumnOut.push({...tableColumnOutputObj})
          })
          this.runType = this.configFlow.runType;
          this.modelMode = res.body.data.modelDTO.modelMode
          this.dialogService.open(modelHistory, {context: {action: ''}, closeOnEsc: true, hasBackdrop: true})
        } else {
          this.isLoading = true;
          setTimeout(() => this.isLoading = false, 10000)
          this.toastrService.danger(res.error.message, 'Error', {icon: 'alert-triangle-outline'});
        }
      }, error => {
        this.isLoading = true;
        setTimeout(() => this.isLoading = false, 10000)
        this.toastrService.danger(error.error.message, 'Error', {icon: 'alert-triangle-outline'});
      },
    )
  }

  getTableColumn(trainingTable: string, connectionId: number) {
    this.isLoading = true;
    let colunmArr = [];
    const colunmProductArr = [];
    const tableColumnFeaObj: Object = {name: '', type: ''}
    this.sqlQueryModel.connectionId = connectionId;
    this.sqlQueryModel.sql = trainingTable
    this.sqlQueryModel.params = {'partition': 6};
    this.hiveDbService.dataSql(this.sqlQueryModel).subscribe(
      res => {
        if (res.body.code === '00') {
          this.tableColumn = res.body.data;
          this.keyTableColumn = res.body.data[0]
          colunmArr = Object.keys(this.keyTableColumn);
          colunmArr.forEach(
            i => {
              tableColumnFeaObj['name'] = i;
              colunmProductArr.push({...tableColumnFeaObj})
            }
          )
          this.tableColumnFea = [...colunmProductArr];
          this.tableColumnOut = [...colunmProductArr];
          this.trainingTable = res.body.data;
          this.targetColumnOut.forEach(item =>
            this.tableColumnOut.splice(this.tableColumnOut.indexOf(item), 1)
          )
          this.targetColumnFea.forEach(item => {
              this.tableColumnFea.splice(this.tableColumnFea.indexOf(item), 1)
            }
          )
          this.isLoading = false;
        }
      }, error => {
        this.toastrService.danger(error.error.message, 'Error');
        this.isLoading = false;
      }
    )
  }

  getDataSQL(sqlQuery, param ?: string) {
    this.isLoading = true;
    this.sqlQueryModel.sql = sqlQuery;
    this.sqlQueryModel.connectionId = this.connectionId;
    const paramsObj = {};
    this.rowParameter.forEach(i => {
        paramsObj[i.key] = i.value
      }
    )
    this.sqlQueryModel.params = paramsObj;
    this.hiveDbService.dataSql(this.sqlQueryModel).subscribe(
      res => {
        if (res.body.code === '00') {
          this.isLoading = false;
          this.keyDataSql = res.body.data[0];
          this.dataSql = res.body.data;
          this.sqlError = '';
        } else {
          this.sqlError = res.error.message
          this.isLoading = false;
        }
      }, error => {
        this.sqlError = 'Format of SQL is wrong';
        this.isLoading = false;
      },
    )
  }

  refreshLog(index?: number) {
    this.modelDetailService.refreshLog(this.modelId).subscribe();
    this.getLog(index);
    this.getDetail();
  }

  getLog(index?: number) {
    this.isLoading = true;
    const historyId = this.modelDetail?.historyDTOS[index].historyId;
    this.modelDetailService.getLogs(historyId).subscribe(
      res2 => {
        if (res2.body.code === '00') {
          this.logData = res2.body.data;
          this.log = [];
          this.logData.forEach(
            i => {
              if (i.logOutput) {
                this.log.push(i.notebookId + ': ' + i.logOutput)
                this.log.push('------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------')
              }
            }
          )
          this.isLoading = false;
        } else {
          this.sqlError = res2.error.message
          this.isLoading = false;
          this.toastrService.danger(res2.error.message, 'Error')
        }
      }, error => {
        this.toastrService.danger(error.error.message, 'Error')
        this.isLoading = false;
      },
    )
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
