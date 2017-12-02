import { Component, Input } from '@angular/core';
import { Song } from "./song";
import { PlayerService } from "../player/player.service";
import { Observable } from 'rxjs/Observable';

@Component({
  selector: '[song-table]',
  templateUrl: './song-table.component.html'
})
export class SongTableComponent {
  @Input() songs: Array<Song> = [];
  @Input() pending: boolean;

  constructor(private ps: PlayerService) {}

  isPlaying(song, curSong): boolean {
    return curSong != null && curSong.id == song.id;
  }

  getPlayButtonClass(song: Song, curSong: Song): string {
    let playing = this.ps.playing.getValue();
    if (playing && curSong.id == song.id) {
      return 'fa-pause-circle-o';
    }
    return 'fa-play-circle-o';
  }

  togglePlayback(song: Song, curSong: Song): void {
    let playing: boolean = this.ps.playing.getValue();

    if (playing) {
      this.ps.pause();
    } else {
      let newSong: Song = null;
      if (!this.isPlaying(song, curSong)) {
        newSong = song;
      }
      this.ps.play(newSong);
    }
  }

  get currentSong(): Observable<Song> {
    return this.ps.song;
  }
}
