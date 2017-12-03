import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CommonModule as ACommonModule } from "../common/common.module";
import { SongsComponent } from './songs.component';
import { SongsRoutingModule } from './songs-routing.module';
import { SongTableComponent } from "./song-table.component";
import {ToSongListPipe} from "./to-song-list.pipe";

@NgModule({
  imports: [
    SongsRoutingModule,
    CommonModule,
    ACommonModule
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
