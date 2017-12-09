import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { TruncateModule } from 'ng2-truncate';
import { ContextMenuModule } from 'ngx-contextmenu';
import { MomentModule } from 'angular2-moment';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { SharedModule } from "../shared/shared.module";
import { AlbumListComponent } from "./album-list.component";
import { AlbumService } from "./album.service";
import { AlbumDetailComponent } from "./album-detail.component";
import { AuthGuard } from "../auth/auth-guard.service";
import { AlbumDetailResolver } from "./album-resolver.service";
import { SongsModule } from "../songs/songs.module";

const routes: Routes = [
  {
    path: 'album/:id',
    component: AlbumDetailComponent,
    canActivate: [ AuthGuard ],
    resolve: {
      album: AlbumDetailResolver
    }
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    TruncateModule,
    NgbModule,
    ContextMenuModule,
    SharedModule,
    InfiniteScrollModule,
    MomentModule,
    SongsModule
  ],
  declarations: [
    AlbumListComponent,
    AlbumDetailComponent
  ],
  exports: [
    AlbumListComponent
  ],
  providers: [
    AlbumService,
    AlbumDetailResolver
  ]
})
export class AlbumModule {}
