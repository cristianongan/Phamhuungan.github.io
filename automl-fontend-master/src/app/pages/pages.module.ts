import {NgModule} from '@angular/core';
import {
  NbAccordionModule, NbAlertModule,
  NbButtonModule,
  NbCardModule,
  NbCheckboxModule, NbDatepicker, NbDatepickerModule, NbFormFieldModule,
  NbIconModule,
  NbInputModule, NbLayoutModule, NbListModule,
  NbMenuModule, NbPopoverModule, NbRadioModule,
  NbSelectModule, NbSidebarModule, NbSpinnerModule, NbStepperModule, NbTabsetModule, NbTooltipModule,
} from '@nebular/theme';

import {ThemeModule} from '../@theme/theme.module';
import {PagesComponent} from './pages.component';
import {ECommerceModule} from './e-commerce/e-commerce.module';
import {PagesRoutingModule} from './pages-routing.module';
import {MiscellaneousModule} from './miscellaneous/miscellaneous.module';
import {GojsAngularModule} from 'gojs-angular';
import {NgSelectModule} from '@ng-select/ng-select';
import {ShareModule} from '../share/share.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ColorPickerModule} from 'ngx-color-picker';
import {ChangePasswordComponent} from '../auth-routing/change-password/change-password.component';
import {RequestPasswordComponent} from '../auth-routing/request-password/request-password.component';
import {RequestPasswordCompleteComponent} from '../auth-routing/request-password-complete/request-password-complete.component';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {ListFilterPipe} from '../share/pipe/list-filter.pipe';
import {CKEditorModule} from 'ckeditor4-angular';
import {TreeViewModule} from '@progress/kendo-angular-treeview';
import {DropDownsModule} from '@progress/kendo-angular-dropdowns';
import {NgxTrimDirectiveModule} from 'ngx-trim-directive';
import {TooltipModule} from '@swimlane/ngx-charts';
import {DashboardComponent} from './dashboard/dashboard.component';
import {MonitorComponent} from './monitors/monitor-models/monitor.component';
import {RunModelsComponent} from './run-models/run-new-models/run-models.component';
import {IntroductionComponent} from './introduction/introduction.component';
import {HighchartsChartModule} from 'highcharts-angular';
import {PickListModule} from 'primeng/picklist';
import {RatingModule} from 'primeng/rating';
import {ButtonModule} from 'primeng/button';
import {CarouselModule} from 'primeng/carousel';
import { MonitorProjectComponent } from './monitors/monitor-project/monitor-project.component';
import { RunExistingModelComponent } from './run-models/run-existing-model/run-existing-model.component';
import { ModelDetailComponent } from './model-detail/model-detail.component';

@NgModule({
    imports: [
        PagesRoutingModule,
        ThemeModule,
        NbMenuModule,
        ECommerceModule,
        MiscellaneousModule,
        GojsAngularModule,
        NbCardModule,
        NbInputModule,
        NbSelectModule,
        NbButtonModule,
        NbIconModule,
        NgxDatatableModule,
        NbCheckboxModule,
        NgSelectModule,
        NbDatepickerModule,
        NbRadioModule,
        ShareModule,
        FormsModule,
        ReactiveFormsModule,
        ColorPickerModule,
        NbFormFieldModule,
        NbSpinnerModule,
        CKEditorModule,
        NbAccordionModule,
        NbTooltipModule,
        NbAlertModule,
        NbListModule,
        NbLayoutModule,
        NbPopoverModule,
        NbSidebarModule,
        TreeViewModule,
        DropDownsModule,
        NgxTrimDirectiveModule,
        TooltipModule,
        NbStepperModule,
        HighchartsChartModule,
        NbTabsetModule,
        PickListModule,
        RatingModule,
        ButtonModule,
      CarouselModule
    ],
  declarations: [
    PagesComponent,
    ChangePasswordComponent,
    RequestPasswordComponent,
    RequestPasswordCompleteComponent,
    ListFilterPipe,
    DashboardComponent,
    MonitorComponent,
    RunModelsComponent,
    IntroductionComponent,
    MonitorProjectComponent,
    RunExistingModelComponent,
    ModelDetailComponent
  ],
  entryComponents: [
    ChangePasswordComponent
  ],
})
export class PagesModule {
}
