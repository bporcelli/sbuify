import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { SearchComponent } from "./search.component";
import { SearchRoutingModule } from './search-routing.module';
import { SharedModule } from "../shared/shared.module";
import { SongsModule } from "../songs/songs.module";
import { AlbumModule } from "../album/album.module";
import { ArtistModule } from "../artist/artist.module";

@NgModule({
  imports: [
    SearchRoutingModule,
    CommonModule,
    SharedModule,
    NgbModule,
    InfiniteScrollModule,
    SongsModule,
    AlbumModule,
    ArtistModule
  ],
  declarations: [
    SearchComponent
  ]
})
export class SearchModule {}
