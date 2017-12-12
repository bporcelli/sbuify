import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { BrowseComponent } from './browse.component';
import { BrowseRoutingModule } from './browse-routing.module';
import { OverviewComponent } from './overview.component';
import { GenresComponent } from './genres/genres.component';
import { NewReleasesComponent } from './new-releases.component';
import { GenreResolver } from "./genres/genre-resolver.service";
import { GenreDetailResolver } from "./genres/genre-detail-resolver.service";
import { GenreComponent } from "./genres/genre.component";
import { AlbumModule } from "../album/album.module";
import { SharedModule } from "../shared/shared.module";
import { UserModule } from "../user/user.module";

@NgModule({
  imports: [
    BrowseRoutingModule,
    CommonModule,
    AlbumModule,
    SharedModule,
    InfiniteScrollModule,
    UserModule
  ],
  declarations: [
    BrowseComponent,
    OverviewComponent,
    GenresComponent,
    GenreComponent,
    NewReleasesComponent,
  ],
  providers: [
    GenreResolver,
    GenreDetailResolver
  ]
})
export class BrowseModule {}
