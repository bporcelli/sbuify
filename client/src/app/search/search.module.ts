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
import { PlaylistModule } from "../playlist/playlist.module";

@NgModule({
  imports: [
    SearchRoutingModule,
    CommonModule,
    SharedModule,
    NgbModule,
    InfiniteScrollModule,
    SongsModule,
    AlbumModule,
    ArtistModule,
    PlaylistModule
  ],
  declarations: [
    SearchComponent
  ]
})
export class SearchModule {}
