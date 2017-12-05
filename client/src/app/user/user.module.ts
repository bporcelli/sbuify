import { NgModule } from '@angular/core';

import { UserService } from "./user.service";
import { APIClient } from "../api/api-client.service";
import { PreferencesService } from "./preferences.service";

@NgModule({
  providers: [
    UserService,
    PreferencesService,
    APIClient
  ]
})
export class UserModule {}
