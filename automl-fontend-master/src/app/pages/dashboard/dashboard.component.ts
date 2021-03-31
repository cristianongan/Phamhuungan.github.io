import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
require('highcharts/themes/dark-blue')(Highcharts);
import {NbDialogService} from '@nebular/theme';
import {ModelDetailComponent} from '../model-detail/model-detail.component';
import {AutoMLChartModel, Ichart} from '../../@core/model/autoML-Chart.model';
import {RecentProjectService} from '../../@core/mock/recentProject.service';
import {IrecentProject} from '../../@core/model/recentProject.model';
import {ProjectService} from '../../@core/mock/project.service';
import {Iproject} from '../../@core/model/project.model';

@Component({
  selector: 'ngx-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  chartML: AutoMLChartModel;
  val = 3;
  columns = [
    {name: 'Model Name', prop: 'modelName', flex: 1},
    {name: 'Project', prop: 'projectName', flex: 1},
    // {name: 'Task', prop: 'tasks', flex: 1},
    {name: '', prop: 'actions', flex: 0.5},
  ];
  Highcharts: typeof Highcharts = Highcharts;
  chartOptions = {
    chart: {
      plotBackgroundColor: null,
      plotBorderWidth: null,
      plotShadow: false,
      type: 'pie',
    },
    title: {
      text: '',
    },
    tooltip: {
      pointFormat: '{point.y}: <b>{point.percentage:.1f}%</b>',
    },
    accessibility: {
      point: {
        valueSuffix: '%',
      },
    },
    plotOptions: {
      pie: {
        allowPointSelect: true,
        cursor: 'pointer',
        dataLabels: {
          enabled: false,
        },
        showInLegend: true,
      },
    },
    series: [
      {
        data: [],
        type: 'pie',
      },
    ],
  };
  chartUpdate = false;
  chartDataName = {
    0: 'Ready',
    1: 'Running',
    2: 'Pending',
    3: 'Done',
    4: 'Cancelled',
    5: 'Error',
  }
  chartData: any;
  chart: Ichart[];
  recentProject: IrecentProject[];
  project: Iproject[];

  constructor(
    public dialogService: NbDialogService,
    private recentProjectService: RecentProjectService,
    private projectService: ProjectService) {
  }

  ngOnInit(): void {
    this.getDataChart();
    this.getRencentProject();
    this.getProject();
  }

  getDataChart() {
    this.recentProjectService.getChart().subscribe(res => {
        if (res.body.code === '00') {
          this.chart = res.body.data;
          this.chartData = this.chart.map(item => {
            return {
              name: this.chartDataName[item.statusCode],
              y: item.percent,
              color: Highcharts.getOptions().colors[item.statusCode],
            };
          });
          this.chartOptions.series.forEach(seri => {
            seri.data = this.chartData;
          })
          this.chartUpdate = true;
        }
      },
    );
  }

  getProject() {
    this.projectService.getProject().subscribe(res => {
        if (res.body.code === '00') {
          this.project = res.body.data;
        }
      },
    );
  }

  getRencentProject() {
    this.recentProjectService.getRecentProject().subscribe(res => {
        if (res.body.code === '00') {
          this.recentProject = res.body.data;
        }
      },
    );
  }

  modelDetail(index: number) {
    this.dialogService.open(ModelDetailComponent, {
      closeOnEsc: true,
      context: {
        modelId: this.recentProject[index].modelId,
      },
    });
  }
}
