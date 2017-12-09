import { Component, OnInit } from '@angular/core';
import { LibraryService } from "./library.service";
import { SongList } from "../songs/song-list";
import { PlaylistSong } from "../playlist/playlist-song";
import { Config } from "../config";
import { Song } from "../songs/song";
import { PlayerService } from "../playback/player.service";

@Component({
  templateUrl: './songs.component.html',
})
export class SongsComponent implements OnInit {

  /** Current filter value */
  private filter: string = '';

  /** Full list of songs. */
  private unfiltered: Song[] = [];

  /** Filtered songs. */
  private songList: SongList = new SongList([]);

  /** Current page in songs list. */
  private page: number = 0;

  /** Is there a pending request for the next page of songs? */
  public pending: boolean = false;

  /** Are there more results available? */
  private more: boolean = true;

  constructor(
    private libService: LibraryService,
    private playService: PlayerService
  ) {}

  ngOnInit(): void {
    this.nextPage();
  }

  /** Update visible rows when filter changes */
  onFilterChange(value: string = null): void {
    if (value == null) {  // use existing filter value
      value = this.filter;
    }

    value = value.toLowerCase();

    const temp = this.unfiltered.filter((song: Song) => {
      return song.name.toLowerCase().indexOf(value) !== -1 || !value;
    });

    this.songList.songs = temp;
    this.filter = value;
  }

  /** Toggle playback of the song list */
  togglePlayback(): void {
    if (!this.isPlaylist()) {
      this.playService.play(this.songList);
    } else {
      this.playService.toggle();
    }
  }

  /** Check: is the song list playing? */
  isPlaying(): boolean {
    return this.isPlaylist() && this.playService.playing.value;
  }

  /** Handle song removal */
  onSongRemoved(song: Song): void {
    this.unfiltered = this.unfiltered.filter(item => item.id != song.id);
    this.onFilterChange();
  }

  private isPlaylist(): boolean {
    return this.playService.isPlaying(this.songList);
  }

  private nextPage() {
    if (this.pending) {  // already waiting for next page
      return;
    } else if (!this.more) {  // no more results available
      return;
    }

    this.pending = true;

    this.libService.getSongs(this.page)
      .subscribe(
        (songs: PlaylistSong[]) => this.handleNewSongs(songs),
        (err: any) => this.handleError(err)
      );
  }

  private handleNewSongs(plSongs: PlaylistSong[]) {
    this.pending = false;

    let songs = plSongs.map(plSong => plSong.song);

    this.unfiltered.push(...songs);
    this.onFilterChange();
    this.page += 1;

    if (songs.length < Config.ITEMS_PER_PAGE) {
      this.more = false;
    }
  }

  private handleError(err: any) {
    // todo: display error
    console.log('error getting next page of songs:', err);
  }

  get songs(): SongList {
    return this.songList;
  }
}
