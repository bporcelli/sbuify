import { NgModule } from '@angular/core';
import { LibraryRoutingModule } from "./library-routing.module";
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { CommonModule } from "@angular/common";
import { SongsComponent } from "./songs.component";
import { AlbumsComponent } from "./albums.component";
import { AlbumModule } from "../album/album.module";
import { SongsModule } from "../songs/songs.module";
import { FilterBoxComponent } from "./filter-box.component";
import { ArtistsComponent } from "./artists.component";
import { ArtistModule } from "../artist/artist.module";

@NgModule({
  imports: [
    LibraryRoutingModule,
    InfiniteScrollModule,
    CommonModule,
    AlbumModule,
    SongsModule,
    ArtistModule
  ],
  declarations: [
    SongsComponent,
    AlbumsComponent,
    FilterBoxComponent,
    ArtistsComponent
  ]
})
export class LibraryModule {}
