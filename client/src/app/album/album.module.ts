import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { TruncateModule } from 'ng2-truncate';
import { ContextMenuModule } from 'ngx-contextmenu';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CommonModule as ACommonModule } from "../common/common.module";
import { AlbumListComponent } from "./album-list.component";
import { AlbumService } from "./album.service";
import { AlbumsComponent } from "./albums.component";

const routes: Routes = [
  { path: 'albums', component: AlbumsComponent }
];

@NgModule({
  imports: [
    CommonModule,
    TruncateModule,
    NgbModule,
    ContextMenuModule,
    ACommonModule,
    InfiniteScrollModule,
    RouterModule.forChild(routes)
  ],
  declarations: [
    AlbumListComponent,
    AlbumsComponent
  ],
  exports: [
    AlbumListComponent,
    RouterModule
  ],
  providers: [
    AlbumService
  ]
})
export class AlbumModule {}
