import { Component, Input } from '@angular/core';
import { Router } from "@angular/router";
import { Song } from "./song";
import { PlayerService } from "../player/player.service";
import { Playable } from "../player/playable";
import { PlayQueueService } from "../player/play-queue.service";
import { PlayQueue } from "../player/play-queue";
import { SongList } from "./song-list";

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

  constructor(private ps: PlayerService,
              private pqs: PlayQueueService,
              private router: Router) {}

  /**
   * Determine whether a song is currently playing.
   * @returns {boolean}
   */
  isPlaying(song: Song): boolean {  // must pass curSong to make idempotent
    return this.ps.isPlaying(song);
  }

  /**
   * Get class for song play button based on whether it is playing or not.
   * @returns {string}
   */
  getPlayButtonClass(song: Song): string {
    let playing = this.ps.playing.getValue();

    if (playing && this.isPlaying(song)) {
      return 'fa-pause-circle-o';
    }
    return 'fa-play-circle-o';
  }

  /**
   * Toggle playback of the song at a given index in the song table.
   * @param {number} index
   */
  togglePlayback(index: number): void {
    index += this.offset;

    let song = this.playlist.songs[index];

    if (this.isPlaying(song)) {  // song is current song
      this.ps.toggle();
    } else if (this.playlist instanceof PlayQueue) {  // song is being played in queue
      this.pqs.skip(index);
      this.ps.play(new SongList([song]));
    } else {
      this.ps.play(this.playlist, index);
    }
  }

  /** Enqueue a song. */
  enqueue(song: Song): void {
    this.pqs.add(song);
  }

  /** Remove an enqueued song. */
  remove(song: Song): void {
    this.pqs.remove(song);
  }

  /** Check whether a song is enqueued */
  isEnqueued(item: any): boolean {
    return item != null && item['is_enqueued'];
  }

  /** Open the album page for a song */
  openAlbumPage(song: Song): void {
    this.router.navigate(['/album', song.album.id]);
  }

  /** Open the artist page for a song */
  openArtistPage(song: Song): void {
    this.router.navigate(['/artist', song.album.artist.id]);
  }

  get songs(): Array<Song> {
    return this.playlist.songs.slice(this.offset).map((s: Song) => {
      s['is_enqueued'] = this.pqs.contains(s);
      return s;
    });
  }
}
