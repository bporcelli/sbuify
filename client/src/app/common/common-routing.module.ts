import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UpgradeAccountComponent } from './upgrade-account.component';

const commonRoutes: Routes = [
  {
    path: 'upgrade',
    component: UpgradeAccountComponent,
    outlet: 'modal'
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(commonRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class CommonRoutingModule {}
