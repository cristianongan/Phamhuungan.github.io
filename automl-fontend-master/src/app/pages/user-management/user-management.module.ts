import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  NbButtonModule,
  NbCardModule,
  NbCheckboxModule, NbDatepickerModule, NbDialogModule,
  NbIconModule,
  NbInputModule, NbLayoutModule, NbListModule,
  NbSelectModule, NbTabsetModule, NbToastrModule, NbToggleModule
} from '@nebular/theme';
import {NgSelectModule} from '@ng-select/ng-select';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {UserManagementComponent} from './user-management.component';
import {UserRoutingModule} from './user-management.route';
import {UserConfigComponent} from './user-config/user-config.component';
import { UserConfigUpdateComponent } from './user-config-update/user-config-update.component';
import {NgxTrimDirectiveModule} from 'ngx-trim-directive';
import {TranslateModule} from '@ngx-translate/core';
import {ConfirmDialogComponent} from '../../share/component/confirm-dialog/confirm-dialog.component';
import {ShareModule} from '../../share/share.module';

@NgModule({
  declarations: [UserManagementComponent, UserConfigComponent, UserConfigUpdateComponent],
  imports: [
    CommonModule,
    UserRoutingModule,
    NbCardModule,
    NbButtonModule,
    NbIconModule,
    NbCheckboxModule,
    NbInputModule,
    NbSelectModule,
    NbTabsetModule,
    NbLayoutModule,
    NbListModule,
    NgSelectModule,
    NbToggleModule,
    FormsModule,
    ReactiveFormsModule,
    NbInputModule,
    Ng2SmartTableModule,
    NgxDatatableModule,
    ReactiveFormsModule,
    NbDialogModule.forChild(),
    NbDatepickerModule,
    NgxTrimDirectiveModule,
    TranslateModule,
    ShareModule,
  ],
  exports: [
  ],
  entryComponents: [
    ConfirmDialogComponent,
    UserConfigUpdateComponent
  ]
})
export class UserManagementModule {}
