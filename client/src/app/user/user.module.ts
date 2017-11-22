import { NgModule } from '@angular/core';

import { UserService } from "./user.service";
import { APIClient } from "../api/api-client.service";

@NgModule({
  providers: [
    UserService,
    APIClient
  ]
})
export class UserModule {}
