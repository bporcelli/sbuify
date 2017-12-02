import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { SearchComponent } from "./search.component";
import { SearchRoutingModule } from './search-routing.module';
import { CommonModule as ACommonModule } from "../common/common.module";
import { SongsModule } from "../songs/songs.module";
import { AlbumModule } from "../album/album.module";

@NgModule({
  imports: [
    NgbModule,
    SearchRoutingModule,
    CommonModule,
    ACommonModule,
    SongsModule,
    AlbumModule,
    InfiniteScrollModule
  ],
  declarations: [
    SearchComponent
  ]
})
export class SearchModule {}
