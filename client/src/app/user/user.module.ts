import { NgModule } from '@angular/core';
import { UserService } from "./user.service";
import { PreferencesService } from "./preferences.service";
import { UpgradeAccountComponent } from "./upgrade-account.component";

@NgModule({
  providers: [
    UserService,
    PreferencesService
  ],
  declarations: [
    UpgradeAccountComponent
  ]
})
export class UserModule {}
