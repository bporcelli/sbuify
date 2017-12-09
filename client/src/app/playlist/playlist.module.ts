import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MomentModule } from 'angular2-moment';
import { ContextMenuModule } from 'ngx-contextmenu';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { TruncateModule } from 'ng2-truncate';
import { PlaylistDetailComponent } from './playlist-detail.component';
import { PlaylistDetailResolver } from './playlist-resolver.service';
import { AuthGuard } from '../auth/auth-guard.service';
import { PlaylistModalComponent } from "./playlist-modal.component";
import { PlaylistFolderModalComponent } from "./playlist-folder-modal.component";
import { PlaylistFolderComponent } from "./playlist-folder.component";
import { SongsModule } from "../songs/songs.module";
import { SharedModule } from "../shared/shared.module";
import { PlaylistGridComponent } from "./playlist-grid.component";

const routes: Routes = [
  {
    path: 'playlist/:id',
    component: PlaylistDetailComponent,
    canActivate: [ AuthGuard ],
    resolve: {
      playlist: PlaylistDetailResolver
    }
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    NgbModule,
    MomentModule,
    FormsModule,
    ContextMenuModule,
    InfiniteScrollModule,
    TruncateModule,
    SongsModule,
    SharedModule
  ],
  exports: [
    PlaylistFolderComponent,
    PlaylistGridComponent
  ],
  declarations: [
    PlaylistDetailComponent,
    PlaylistModalComponent,
    PlaylistFolderModalComponent,
    PlaylistFolderComponent,
    PlaylistGridComponent
  ],
  entryComponents: [
    PlaylistModalComponent,
    PlaylistFolderModalComponent
  ],
  providers: [
    PlaylistDetailResolver
  ]
})
export class PlaylistModule {}
