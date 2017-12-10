import { NgModule } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { UserService } from "./user.service";
import { PreferencesService } from "./preferences.service";
import { UpgradeAccountModalComponent } from "./upgrade-account-modal.component";

@NgModule({
  imports: [
    FormsModule,
    CommonModule
  ],
  providers: [
    UserService,
    PreferencesService
  ],
  declarations: [
    UpgradeAccountModalComponent
  ],
  entryComponents: [
    UpgradeAccountModalComponent
  ]
})
export class UserModule {}
