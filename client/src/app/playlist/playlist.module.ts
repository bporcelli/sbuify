import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MomentModule } from 'angular2-moment';
import { ContextMenuModule } from 'ngx-contextmenu';
import { PlaylistDetailComponent } from './playlist-detail.component';
import { PlaylistDetailResolver } from './playlist-resolver.service';
import { AuthGuard } from '../auth/auth-guard.service';
import { PlaylistListComponent } from "./playlist-list.component";
import { PlaylistModalComponent } from "./playlist-modal.component";
import { PlaylistFolderModalComponent } from "./playlist-folder-modal.component";
import { PlaylistFolderComponent } from "./playlist-folder.component";

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
    ContextMenuModule
  ],
  exports: [
    PlaylistListComponent
  ],
  declarations: [
    PlaylistDetailComponent,
    PlaylistListComponent,
    PlaylistModalComponent,
    PlaylistFolderModalComponent,
    PlaylistFolderComponent
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
