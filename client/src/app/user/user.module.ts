import { NgModule } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { UserService } from "./user.service";
import { PreferencesService } from "./preferences.service";
import { UpgradeAccountModalComponent } from "./upgrade-account-modal.component";
import { UserLocationResolver } from "./user-location-resolver.service";
import { ProfileComponent } from "./profile.component";
import { AuthGuard } from "../auth/auth-guard.service";
import { CustomerDetailsResolver } from "./customer-details-resolver.service";
import { SharedModule } from "../shared/shared.module";
import { ProfilePictureModalComponent } from "./profile-picture-modal.component";

const routes: Routes = [
  {
    path: 'customer/:id',
    component: ProfileComponent,
    canActivate: [ AuthGuard ],
    resolve: {
      user: CustomerDetailsResolver
    }
  }
];

@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    RouterModule.forChild(routes),
    SharedModule
  ],
  providers: [
    UserService,
    PreferencesService,
    UserLocationResolver,
    CustomerDetailsResolver
  ],
  declarations: [
    UpgradeAccountModalComponent,
    ProfileComponent,
    ProfilePictureModalComponent
  ],
  entryComponents: [
    UpgradeAccountModalComponent,
    ProfilePictureModalComponent
  ]
})
export class UserModule {}
