import { NgModule } from '@angular/core';
import { LibraryRoutingModule } from "./library-routing.module";
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { CommonModule } from "@angular/common";
import { SongsComponent } from "./songs.component";
import { AlbumsComponent } from "./albums.component";
import { AlbumModule } from "../album/album.module";
import { SongsModule } from "../songs/songs.module";
import { ArtistsComponent } from "./artists.component";
import { ArtistModule } from "../artist/artist.module";
import { SharedModule } from "../shared/shared.module";

@NgModule({
  imports: [
    LibraryRoutingModule,
    InfiniteScrollModule,
    CommonModule,
    AlbumModule,
    SongsModule,
    ArtistModule,
    SharedModule
  ],
  declarations: [
    SongsComponent,
    AlbumsComponent,
    ArtistsComponent
  ]
})
export class LibraryModule {}
