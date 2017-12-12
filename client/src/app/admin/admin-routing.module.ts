import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminHomeComponent } from "./admin-home.component";
import { AdminAuthGuard } from "../auth/admin-auth-guard.service";
import { UserListComponent } from "./user-list.component";
import { CreateUserComponent } from "./create-user.component";
import { RoyaltiesComponent } from "./royalties.component";

const routes: Routes = [
  {
    path: 'admin',
    canActivate: [ AdminAuthGuard ],
    canActivateChild: [ AdminAuthGuard ],
    children: [
      {
        path: 'home',
        component: AdminHomeComponent,
      },
      {
        path: 'users',
        component: UserListComponent
      },
      {
        path: 'create-user',
        component: CreateUserComponent
      },
      {
        path: 'royalties',
        component: RoyaltiesComponent
      },
      {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AdminRoutingModule {}
