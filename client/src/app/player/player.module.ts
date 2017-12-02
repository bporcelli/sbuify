import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PlaybarComponent } from "./playbar.component";
import { PlayerRoutingModule } from "./player-routing.module";
import { PlayQueueComponent } from "./play-queue.component";
import { CommonModule as ACommonModule } from "../common/common.module";

@NgModule({
  declarations: [
    PlaybarComponent,
    PlayQueueComponent
  ],
  imports: [
    CommonModule,
    ACommonModule,
    PlayerRoutingModule
  ],
  exports: [
    PlaybarComponent
  ]
})
export class PlayerModule {}
