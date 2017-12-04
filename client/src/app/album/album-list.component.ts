import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Album } from "./album";
import { PlayerService } from "../player/player.service";
import { PlayQueueService } from "../player/play-queue.service";

@Component({
  selector: '[album-list]',
  templateUrl: './album-list.component.html'
})
export class AlbumListComponent {
  @Input() albums: Array<Album>;
  @Input() pending: boolean;

  constructor(private ps: PlayerService,
              private pqs: PlayQueueService,
              private router: Router) {}

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

  /** Add album to play queue */
  enqueue(album: Album): void {
    this.pqs.add(album);
  }

  /** Navigate to album page */
  openAlbumPage(album: Album): void {
    this.router.navigate(['/album', album.id]);
  }

  /** Navigate to album artist page */
  openArtistPage(album: Album): void {
    this.router.navigate(['/artist', album.artist.id]);
  }
}
