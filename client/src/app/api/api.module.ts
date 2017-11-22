import { NgModule } from '@angular/core';
import { APIClient } from "./api-client.service";

@NgModule({
  providers: [
    APIClient
  ],
  exports: [
    APIClient
  ]
})
export class APIModule {}
