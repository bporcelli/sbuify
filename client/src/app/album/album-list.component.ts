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

  playAlbum(event: Event, album: Album) {
    event.stopPropagation();
    event.preventDefault();

    this.ps.play(album);
  }

  isPlaying(album: Album) {
    // todo
    return false;
  }
}
