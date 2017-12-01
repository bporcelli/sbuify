import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { SearchComponent } from "./search.component";
import { SearchRoutingModule } from './search-routing.module';
import { CommonModule as ACommonModule } from "../common/common.module";

@NgModule({
  imports: [
    NgbModule,
    SearchRoutingModule,
    CommonModule,
    ACommonModule,
    InfiniteScrollModule
  ],
  declarations: [
    SearchComponent
  ]
})
export class SearchModule {}
