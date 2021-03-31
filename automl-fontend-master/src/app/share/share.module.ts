import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ColorPickerComponent} from './component/color-picker/color-picker.component';
import {NbButtonModule, NbCardModule, NbInputModule} from '@nebular/theme';
import {ColorPickerModule} from 'ngx-color-picker';
import {BorderSelectComponent} from './component/border-select/border-select.component';
import {NgSelectModule} from '@ng-select/ng-select';
import {ConfirmDialogComponent} from './component/confirm-dialog/confirm-dialog.component';
import {InlineMessageComponent} from './component/inline-message/inline-message.component';
import {TranslateModule} from '@ngx-translate/core';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {CarouselModule} from 'ngx-owl-carousel-o';

@NgModule({
  declarations: [
    ColorPickerComponent,
    BorderSelectComponent,
    ConfirmDialogComponent,
    InlineMessageComponent
  ],
  imports: [
    CommonModule,
    NbCardModule,
    ColorPickerModule,
    NgSelectModule,
    TranslateModule,
    ReactiveFormsModule,
    NbButtonModule,
    NbInputModule,
    RouterModule,
    FormsModule,
    CarouselModule,
  ],
  entryComponents: [
    ColorPickerComponent,
    ConfirmDialogComponent,
    InlineMessageComponent
  ],
  exports: [
    BorderSelectComponent,
    ColorPickerComponent,
    ConfirmDialogComponent,
    InlineMessageComponent,
    TranslateModule
  ],
})
export class ShareModule {
}
