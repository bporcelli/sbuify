import { Component, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { Song } from "./song";
import { PlayerService } from "../playback/player.service";
import { Playable } from "../playback/playable";
import { PlayQueueService } from "../play-queue/play-queue.service";
import { PlayQueue } from "../play-queue/play-queue";
import { SongList } from "./song-list";
import { SongContextMenuComponent } from "./song-context-menu.component";
import { LibraryService } from "../library/library.service";
import { Playlist } from "../playlist/playlist";

@Component({
  selector: '[song-table]',
  templateUrl: './song-table.component.html'
})
export class SongTableComponent {

  @ViewChild(SongContextMenuComponent) menu: SongContextMenuComponent;

  /** Playlist if the table is being displayed on a playlist page */
  @Input() playlist: Playlist = null;

  /** Songs to display */
  @Input() songList: Playable = null;

  /** Offset to first song */
  @Input() offset: number = 0;

  /** Boolean flag to indicate whether the songs for the table are loading */
  @Input() pending: boolean;

  /** Event emitted to parent components when a song is removed */
  @Output() onRemoved: EventEmitter<any> = new EventEmitter();

  constructor(
    private ps: PlayerService,
    private pqs: PlayQueueService,
    private libService: LibraryService
  ) {}

  /**
   * Determine whether a song is currently playing.
   * @returns {boolean}
   */
  isPlaying(song: Song): boolean {
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

    let song = this.songList.songs[index];

    if (this.isPlaying(song)) {  // song is current song
      this.ps.toggle();
    } else if (this.songList instanceof PlayQueue) {  // song is being played in queue
      this.pqs.skip(index);
      this.ps.play(new SongList([song], 'song'));
    } else {
      this.ps.play(this.songList, index);
    }
  }

  /** Save a song to or remove a song from the customer's library. */
  saveOrRemove(song: Song): void {
    this.libService.saveOrRemove(song)
      .subscribe((saved: boolean) => {
        if (!saved) {
          this.onRemoved.emit(song);
        }
      });
  }

  /** Check whether a song is saved */
  isSaved(item: any): boolean {
    return item != null && this.libService.isSaved(item);
  }

  /** Open the songs context menu */
  onContextMenu(event: MouseEvent, item: any): void {
    this.menu.open(event, item);
  }

  /** Remove a song from the table if required */
  onSongRemoved(song: Song): void {
    this.onRemoved.emit(song);
  }

  get songs(): Array<Song> {
    return this.songList.songs.slice(this.offset);
  }
}
