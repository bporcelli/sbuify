import { NgModule } from '@angular/core';
import { LibraryRoutingModule } from "./library-routing.module";
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from "@angular/common";
import { SongsComponent } from "./songs.component";
import { AlbumsComponent } from "./albums.component";
import { AlbumModule } from "../album/album.module";
import { SongsModule } from "../songs/songs.module";
import { ArtistsComponent } from "./artists.component";
import { ArtistModule } from "../artist/artist.module";
import { SharedModule } from "../shared/shared.module";
import { RecentlyPlayedComponent } from "./recently-played.component";
import { PlaylistModule } from "../playlist/playlist.module";

@NgModule({
  imports: [
    LibraryRoutingModule,
    InfiniteScrollModule,
    CommonModule,
    NgbModule,
    AlbumModule,
    SongsModule,
    ArtistModule,
    SharedModule,
    PlaylistModule
  ],
  declarations: [
    SongsComponent,
    AlbumsComponent,
    ArtistsComponent,
    RecentlyPlayedComponent
  ]
})
export class LibraryModule {}
