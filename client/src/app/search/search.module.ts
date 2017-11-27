import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { SearchComponent } from "./search.component";
import { SearchRoutingModule } from './search-routing.module';
import { FormatDurationPipe } from "../common/format-duration.pipe";

@NgModule({
  imports: [
    NgbModule,
    SearchRoutingModule,
    CommonModule,
    InfiniteScrollModule,
  ],
  declarations: [
    SearchComponent,
    FormatDurationPipe
  ],
  exports: [ FormatDurationPipe ]
})
export class SearchModule {}
