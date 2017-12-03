import { Component, Input } from '@angular/core';
import { Song } from "./song";
import { PlayerService } from "../player/player.service";
import { Observable } from 'rxjs/Observable';
import { Playable } from "../player/playable";

@Component({
  selector: '[song-table]',
  templateUrl: './song-table.component.html'
})
export class SongTableComponent {

  /** Playlist to display */
  @Input() playlist: Playable = null;

  /** Offset to first song */
  @Input() offset: number = 0;

  /** Boolean flag to indicate whether the songs for the table are loading */
  @Input() pending: boolean;

  constructor(private ps: PlayerService) {}

  isPlaying(song: Song, curSong: Song): boolean {  // must pass curSong to make idempotent
    return this.ps.isPlaying(song);
  }

  getPlayButtonClass(song: Song, curSong: Song): string {
    let playing = this.ps.playing.getValue();

    if (playing && this.isPlaying(song, curSong)) {
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

    let song = this.playlist.songs[index];

    if (!this.isPlaying(song, curSong)) {
      this.ps.play(this.playlist, index);
    } else {
      this.ps.toggle();
    }
  }

  get songs(): Array<Song> {
    return this.playlist.songs.slice(this.offset);
  }

  get currentSong(): Observable<Song> {
    return this.ps.song;
  }
}
