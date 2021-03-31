import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {PagesComponent} from './pages.component';
import {NotFoundComponent} from './miscellaneous/not-found/not-found.component';
import {NotPermissionComponent} from './miscellaneous/not-permission/not-permission.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {RunModelsComponent} from './run-models/run-new-models/run-models.component';
import {MonitorComponent} from './monitors/monitor-models/monitor.component';
import {IntroductionComponent} from './introduction/introduction.component';
import {MonitorProjectComponent} from './monitors/monitor-project/monitor-project.component';
import {RunExistingModelComponent} from './run-models/run-existing-model/run-existing-model.component';
import {AccountResolverService} from '../@core/auth/resolve-route';

const routes: Routes = [{
  path: '',
  component: PagesComponent,
  children: [
    {
      path: 'project-list',
      // resolve: {data: AccountResolverService},
      component: MonitorProjectComponent,
    },
    {
      path: 'run-new-models',
      component: RunModelsComponent,
    },
    {
      path: 'run-existing-models',
      component: RunExistingModelComponent,
    },
    {
      path: 'dashboard',
      component: DashboardComponent,
    },
    {
      path: 'model-list',
      component: MonitorComponent,
    },
    {
      path: 'introduction',
      component: IntroductionComponent,
    },
    {
      path: 'forms',
      loadChildren: () => import('./forms/forms.module')
        .then(m => m.FormsModule),
    },
    {
      path: 'ui-features',
      loadChildren: () => import('./ui-features/ui-features.module')
        .then(m => m.UiFeaturesModule),
    },
    {
      path: 'modal-overlays',
      loadChildren: () => import('./modal-overlays/modal-overlays.module')
        .then(m => m.ModalOverlaysModule),
    },
    {
      path: 'extra-components',
      loadChildren: () => import('./extra-components/extra-components.module')
        .then(m => m.ExtraComponentsModule),
    },
    {
      path: 'miscellaneous',
      loadChildren: () => import('./miscellaneous/miscellaneous.module')
        .then(m => m.MiscellaneousModule),
    },
    {
      path: 'not-permission',
      component: NotPermissionComponent
    },
    {
      path: 'not-found',
      component: NotFoundComponent
    },
    {
      path: '',
      redirectTo: 'dashboard',
      pathMatch: 'full',
    },
    {
      path: '**',
      component: NotFoundComponent,
    },
  ],
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {
}
