import { Component, Input } from '@angular/core';
import { Song } from "./song";
import { PlayerService } from "../player/player.service";
import { Observable } from 'rxjs/Observable';
import { SongList } from "../player/song-list";

@Component({
  selector: '[song-table]',
  templateUrl: './song-table.component.html'
})
export class SongTableComponent {

  /** Songs to display in table */
  @Input("songs") _songs: Array<Song> = [];

  /** Offset to first song */
  @Input() offset: number = 0;

  /** Boolean flag to indicate whether the songs for the table are loading */
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

  /**
   * Toggle playback of the song at a given index in the song table.
   * @param {number} index
   */
  togglePlayback(index: number, curSong: Song): void {
    index += this.offset;

    let playing: boolean = this.ps.playing.getValue();
    let song = this._songs[index];

    if (!this.isPlaying(song, curSong)) {
      this.ps.playInList(this._songs, index);
    } else if (playing) {
      this.ps.pause();
    } else {
      this.ps.play();
    }
  }

  get currentSong(): Observable<Song> {
    return this.ps.song;
  }

  get songs(): Array<Song> {
    return this._songs.slice(this.offset);
  }
}
