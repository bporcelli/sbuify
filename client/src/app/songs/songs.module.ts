import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ContextMenuModule } from 'ngx-contextmenu';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CommonModule as ACommonModule } from "../common/common.module";
import { SongTableComponent } from "./song-table.component";
import { ToSongListPipe } from "./to-song-list.pipe";

@NgModule({
  imports: [
    CommonModule,
    ACommonModule,
    ContextMenuModule,
    InfiniteScrollModule,
    RouterModule
  ],
  declarations: [
    SongTableComponent,
    ToSongListPipe
  ],
  exports: [
    SongTableComponent,
    ToSongListPipe
  ]
})
export class SongsModule {}
