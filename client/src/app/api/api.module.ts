import { NgModule } from '@angular/core';
import { APIClient } from "./api-client.service";

@NgModule({
  providers: [
    APIClient
  ]
})
export class APIModule {}
