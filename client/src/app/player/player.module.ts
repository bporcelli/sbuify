import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from "@angular/router";
import { TruncateModule } from 'ng2-truncate';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PlaybarComponent } from "./playbar.component";
import { CommonModule as ACommonModule } from "../common/common.module";
import { SongsModule } from "../songs/songs.module";
import { LyricsModalComponent } from "./lyrics-modal.component";
import { LyricsToHtmlPipe } from "./lyrics-to-html.pipe";

@NgModule({
  declarations: [
    PlaybarComponent,
    LyricsModalComponent,
    LyricsToHtmlPipe
  ],
  entryComponents: [
    LyricsModalComponent
  ],
  imports: [
    RouterModule,
    CommonModule,
    ACommonModule,
    TruncateModule,
    NgbModule,

  ],
  exports: [
    PlaybarComponent
  ]
})
export class PlayerModule {}
