import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from '../auth/auth-guard.service';
import { SearchComponent } from "./search.component";

const searchRoutes: Routes = [
  {
    path: 'search',
    component: SearchComponent,
    canActivate: [ AuthGuard ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(searchRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class SearchRoutingModule {}
