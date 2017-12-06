import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { LibraryService } from "../user/library.service";
import { SongList } from "./song-list";
import { PlaylistSong } from "../playlist/playlist-song";
import { Config } from "../config";
import { Observable } from 'rxjs/Rx';
import { Song } from "./song";
import { PlayerService } from "../player/player.service";

@Component({
  templateUrl: './songs.component.html',
})
export class SongsComponent implements OnInit, AfterViewInit {

  @ViewChild('filterInput') filterInput: ElementRef;

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

  ngAfterViewInit(): void {
    let filterElement = this.filterInput.nativeElement;

    Observable.fromEvent(filterElement, 'input')
      .map((event: Event) => event.target['value'].trim())
      .subscribe((value: string) => this.onFilterChange(value));
  }

  /** Update visible rows when filter changes */
  onFilterChange(value: string = null): void {
    if (value == null) {  // use existing filter value
      value = this.filterInput.nativeElement.value;
    }

    value = value.toLowerCase();

    const temp = this.unfiltered.filter((song: Song) => {
      return song.name.toLowerCase().indexOf(value) !== -1 || !value;
    });

    this.songList.songs = temp;
  }

  /** Reset filter */
  resetFilter(): void {
    // reset filter input
    this.filterInput.nativeElement.value = '';

    // trigger update
    this.onFilterChange();
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

    if (songs.length < Config.SONGS_PER_PAGE) {
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
