import { NgModule } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { UserService } from "./user.service";
import { PreferencesService } from "./preferences.service";
import { UpgradeAccountModalComponent } from "./upgrade-account-modal.component";
import { UserLocationResolver } from "./user-location-resolver.service";

@NgModule({
  imports: [
    FormsModule,
    CommonModule
  ],
  providers: [
    UserService,
    PreferencesService,
    UserLocationResolver
  ],
  declarations: [
    UpgradeAccountModalComponent
  ],
  entryComponents: [
    UpgradeAccountModalComponent
  ]
})
export class UserModule {}
