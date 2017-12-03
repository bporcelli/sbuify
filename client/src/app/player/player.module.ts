import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TruncateModule } from 'ng2-truncate';
import { PlaybarComponent } from "./playbar.component";
import { PlayerRoutingModule } from "./player-routing.module";
import { PlayQueueComponent } from "./play-queue.component";
import { CommonModule as ACommonModule } from "../common/common.module";
import { SongsModule } from "../songs/songs.module";

@NgModule({
  declarations: [
    PlaybarComponent,
    PlayQueueComponent
  ],
  imports: [
    PlayerRoutingModule,
    CommonModule,
    ACommonModule,
    TruncateModule,
    SongsModule
  ],
  exports: [
    PlaybarComponent
  ]
})
export class PlayerModule {}
