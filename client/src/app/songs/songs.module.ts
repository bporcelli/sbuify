import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CommonModule as ACommonModule } from "../common/common.module";
import { SongsComponent } from './songs.component';
import { SongsRoutingModule } from './songs-routing.module';
import { SongTableComponent } from "./song-table.component";

@NgModule({
  imports: [
    SongsRoutingModule,
    CommonModule,
    ACommonModule
  ],
  declarations: [
    SongsComponent,
    SongTableComponent
  ],
  exports: [
    SongTableComponent
  ]
})
export class SongsModule {}
