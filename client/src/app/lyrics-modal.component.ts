import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Song } from "./songs/song";

@Component({
  templateUrl: './lyrics-modal.component.html'
})
export class LyricsModalComponent {
  @Input() song: Song = null;

  constructor(private activeModal: NgbActiveModal) {}

  close(): void {
    this.activeModal.close();
  }
}
