import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login.component';
import { ResetPasswordComponent } from './reset-password.component';
import { RegisterComponent } from './register.component';
import { NoAuthGuard } from "../auth/no-auth-guard";

const loginRoutes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [NoAuthGuard],
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [NoAuthGuard],
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent,
    canActivate: [NoAuthGuard],
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(loginRoutes)
  ],
  exports: [
    RouterModule
  ],
})
export class GuestRoutingModule {}
