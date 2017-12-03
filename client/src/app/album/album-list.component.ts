import { Component, Input } from '@angular/core';
import { Album } from "./album";
import { PlayerService } from "../player/player.service";

@Component({
  selector: '[album-list]',
  templateUrl: './album-list.component.html'
})
export class AlbumListComponent {
  @Input() albums: Array<Album>;
  @Input() pending: boolean;

  constructor(private ps: PlayerService) {}

  toggleAlbumPlayback(event: Event, album: Album) {
    event.stopPropagation();
    event.preventDefault();

    if (!this.isPlaying(album)) {
      this.ps.play(album);
    } else {
      this.ps.toggle();
    }
  }

  getPlayButtonClass(album: Album): string {
    let playing = this.ps.playing.getValue();

    if (playing && this.isPlaying(album)) {
      return 'fa-pause-circle-o';
    }
    return 'fa-play-circle-o';
  }

  isPlaying(album: Album) {
    return this.ps.isPlaying(album);
  }
}
