import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContextMenuModule } from 'ngx-contextmenu';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CommonModule as ACommonModule } from "../common/common.module";
import { SongsComponent } from './songs.component';
import { SongsRoutingModule } from './songs-routing.module';
import { SongTableComponent } from "./song-table.component";
import { ToSongListPipe } from "./to-song-list.pipe";

@NgModule({
  imports: [
    SongsRoutingModule,
    CommonModule,
    ACommonModule,
    ContextMenuModule,
    InfiniteScrollModule
  ],
  declarations: [
    SongsComponent,
    SongTableComponent,
    ToSongListPipe
  ],
  exports: [
    SongTableComponent,
    ToSongListPipe
  ]
})
export class SongsModule {}
