import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TruncateModule } from 'ng2-truncate';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PlaybarComponent } from "./playbar.component";
import { PlayerRoutingModule } from "./player-routing.module";
import { PlayQueueComponent } from "./play-queue.component";
import { CommonModule as ACommonModule } from "../common/common.module";
import { SongsModule } from "../songs/songs.module";
import { LyricsModalComponent } from "./lyrics-modal.component";
import { LyricsToHtmlPipe } from "./lyrics-to-html.pipe";

@NgModule({
  declarations: [
    PlaybarComponent,
    PlayQueueComponent,
    LyricsModalComponent,
    LyricsToHtmlPipe
  ],
  entryComponents: [
    LyricsModalComponent
  ],
  imports: [
    PlayerRoutingModule,
    CommonModule,
    ACommonModule,
    TruncateModule,
    NgbModule,
    SongsModule
  ],
  exports: [
    PlaybarComponent
  ]
})
export class PlayerModule {}
