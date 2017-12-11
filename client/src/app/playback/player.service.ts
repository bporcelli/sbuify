import { Injectable } from '@angular/core';
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { Observable } from 'rxjs/Rx';
import 'rxjs/operators/merge';

import { Song } from "../songs/song";
import { tokenGetter } from "../auth/helpers";
import { PlayQueueService } from "../play-queue/play-queue.service";
import { Playable } from "./playable";
import { PlayQueue } from "../play-queue/play-queue";
import { RepeatMode } from "./repeat-mode";
import { PreferencesService } from "../user/preferences.service";
import { StreamService } from "./stream.service";
import { Playlist } from "../playlist/playlist";

@Injectable()
export class PlayerService {

  /** Audio player */
  private player: any = new Audio();

  /** Current song */
  public song: BehaviorSubject<Song> = new BehaviorSubject(null);

  /** The song, album, or playlist being played */
  public playlist: Playable = null;  // todo: get initial playlist from server?

  /** Songs queued to play */
  public songs: Song[] = [];

  /** Index to current song in playlist */
  public index: number = 0;

  /** Current playback time (secs) */
  private _time: BehaviorSubject<number> = new BehaviorSubject(0);

  /** Duration of the current song (secs) */
  private _duration: BehaviorSubject<number> = new BehaviorSubject(0);

  /** Is the player playing? */
  private _playing: BehaviorSubject<boolean> = new BehaviorSubject(false);

  /** Is playback shuffled? */
  public shuffled: boolean = false;

  /** Current repeat mode */
  public repeat: RepeatMode = RepeatMode.NONE;

  constructor(
    private prefService: PreferencesService,
    private pqs: PlayQueueService,
    private streamService: StreamService
  ) {
    this.initSongObserver();
    this.initPlayingObserver();
    this.initDurationObserver();
    this.initTimeObserver();
    this.initPrefsObserver();
  }

  /** If we don't have anything to play, take the first song from the user's play queue. */
  initSongObserver(): void {
    this.pqs.get().subscribe((pq: PlayQueue) => {
      if (pq != null && this.song.value == null && pq.songs.length > 0) {
        this.next(false);
      }
    });
  }

  initPlayingObserver(): void {
    Observable.merge(
      Observable.fromEvent(this.player, 'play'),
      Observable.fromEvent(this.player, 'pause'),
      Observable.fromEvent(this.player, 'ended')
    ).subscribe((e: Event) => {
      if (e.type == 'ended') {  // playback ended
        this.onPlaybackEnded();
      } else {
        this._playing.next(!this.player.paused);
      }
    });
  }

  initDurationObserver(): void {
    Observable.fromEvent(this.player, 'durationchange').subscribe(() => {
      this._duration.next(this.player.duration);
    });
  }

  initTimeObserver(): void {
    Observable.fromEvent(this.player, 'timeupdate').subscribe(() => {
      this._time.next(this.player.currentTime);
    });
  }

  initPrefsObserver(): void {
    this.prefService.preferences
      .subscribe((preferences: object) => {
        if (preferences) {
          this.shuffled = preferences['shuffle'] == 'true';
          this.repeat = preferences['repeat'];
        }
      });
  }

  play(playable?: Playable, index: number = null): void {
    if (playable != null) {
      this.playlist = playable;
      this.songs = playable.songs.slice();
      this.index = index != null ? index : 0;

      // if a playlist or album was played & shuffle is enabled, shuffle all songs
      if (this.shuffled && index == null) {
        this.shuffle();
      }

      this.setSong(this.songs[this.index]);

      // if a specific song was played & shuffle is enabled, shuffle all songs after the selected one
      if (this.shuffled && index != null) {
        this.shuffle();
      }
    }
    this.player.play();
  }

  pause(): void {
    this.player.pause();
  }

  toggle(): void {
    if (this.player.paused) {
      this.play();
    } else {
      this.pause();
    }
  }

  next(play: boolean = true): void {
    this.setSong(this.getNext());

    if (play) {
      this.player.play();
    }
  }

  prev(play: boolean = true): void {
    this.setSong(this.getPrev());

    if (play) {
      this.player.play();
    }
  }

  hasNext(): boolean {
    return this.playlistHasNext() || this.pqs.hasNext();
  }

  hasPrev(): boolean {
    if (this.playlist == null) {
      return false;
    }
    return this.index > 0;
  }

  playlistHasNext(): boolean {
    if (this.playlist == null) {
      return false;
    }
    return this.index < this.songs.length - 1;
  }

  getNext(): Song {
    if (this.pqs.hasNext()) {
      return this.pqs.next();
    } else if (this.playlistHasNext()) {
      return this.songs[++this.index];
    }
    return null;
  }

  getPrev(): Song {
    if (this.hasPrev()) {
      return this.songs[--this.index];
    }
    return null;
  }

  setSong(song: Song): void {
    if (this.song.value && this.player.played) {
      // record stream of previous song
      let playlist = this.playlist.type == 'playlist' ? this.playlist : null;
      this.streamService.create(this.song.value, this.player.played, <Playlist>playlist);
    }

    this.song.next(song);

    if (song != null) {
      this.player.src = '/api/stream/' + song.id + '/?token=' + tokenGetter()();
    } else {
      this.player.src = '';
    }
  }

  isPlaying(playable: Playable) {
    let song = this.song.getValue();
    let isSong = song != null && song.id == playable.id;
    let isList = this.playlist != null && this.playlist.id == playable.id;

    return isList || isSong;
  }

  /** Toggle playback shuffling */
  toggleShuffle(): void {
    if (!this.shuffled) {
      this.shuffle();
      this.shuffled = true;
    } else {
      this.unshuffle();
      this.shuffled = false;
    }

    // sync with server
    this.prefService.setPreference('shuffle', this.shuffled).subscribe();
  }

  /** Shuffle the songs in the current playlist, leaving the current song in place. */
  private shuffle(): void {
    let songs = this.playlist ? this.songs : [];

    if (songs.length == 0) {  // nothing to shuffle
      return;
    }

    for (let i = 0; i < songs.length; i++) {
      songs[i]['order'] = i;  // save original ordering
    }

    if (this.song.value == null) {
      // shuffle all songs
      songs = this.shuffleArray(songs);
    } else {
      // leave current song in place
      let previous = [];
      if (this.index > 0) {
        previous = <Song[]>this.shuffleArray(songs.slice(0, this.index));
      }
      let current = [this.song.value];
      let upcoming = <Song[]>this.shuffleArray(this.upcoming);

      songs =  previous.concat(current.concat(upcoming));
    }

    this.songs = songs;
  }

  /** Unshuffle the songs in the current playlist, leaving the current song in place. */
  private unshuffle(): void {
    let songs = this.playlist ? this.songs : [];

    if (songs.length == 0) {  // nothing to unshuffle
      return;
    }

    let current = songs[this.index];

    songs.sort((a: Song, b: Song) => {
      if (a['order'] < b['order']) {
        return -1;
      } else if (a['order'] > b['order']) {
        return 1;
      }
      return 0;
    });

    this.index = current['order'];
  }

  /** Shuffle an array using the Durstenfeld shuffle algorithm. */
  private shuffleArray(arr: any[]): any[] {
    for (let i = arr.length - 1; i > 0; i--) {
      let j = Math.floor(Math.random() * (i + 1));
      [arr[i], arr[j]] = [arr[j], arr[i]];
    }
    return arr;
  }

  /** Toggle the repeat mode */
  toggleRepeat(): void {
    if (this.repeat == RepeatMode.NONE) {
      this.repeat = RepeatMode.REPEAT;
    } else if (this.repeat == RepeatMode.REPEAT) {
      this.repeat = RepeatMode.REPEAT_ONE;
    } else {
      this.repeat = RepeatMode.NONE;
    }

    // sync with server
    this.prefService.setPreference('repeat', this.repeat).subscribe();
  }

  /** Advance to the next song when playback of the current song ends */
  private onPlaybackEnded() {
    if (!this.hasNext() && this.repeat == RepeatMode.NONE) {
      return;
    }

    if (this.repeat == RepeatMode.REPEAT && !this.hasNext()) {
      this.setSong(this.songs[this.index = 0]);
    } else if (this.repeat != RepeatMode.REPEAT_ONE) {
      this.setSong(this.getNext());
    }
    this.player.play();
  }

  get time(): BehaviorSubject<number> {
    return this._time;
  }

  setTime(newTime: number): void {  // can't use TS setter b/c types differ
    this.player.currentTime = newTime;
  }

  get duration(): BehaviorSubject<number> {
    return this._duration;
  }

  get muted(): boolean {
    return this.player.muted;
  }

  set muted(muted: boolean) {
    this.player.muted = muted;
  }

  get volume(): number {
    return this.player.volume;
  }

  set volume(vol: number) {
    this.player.volume = vol;
  }

  get playing(): BehaviorSubject<boolean> {
    return this._playing;
  }

  get upcoming(): Array<Song> {
    return this.songs.slice(this.index + 1);
  }
}
