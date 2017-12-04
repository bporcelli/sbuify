import { Injectable } from '@angular/core';
import { Song } from "../songs/song";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { APIClient } from "../api/api-client.service";
import { tokenGetter } from "../auth/helpers";
import { Observable } from 'rxjs/Observable';
import { PlayQueueService } from "./play-queue.service";
import { Playable } from "./playable";
import { PlayQueue } from "./play-queue";

@Injectable()
export class PlayerService {

  // audio player
  player: any = new Audio();

  // current song
  song: BehaviorSubject<Song> = new BehaviorSubject(null);

  // the song, album, or playlist being played
  playlist: Playable = null; // todo: get initial playlist from server?

  // index to current song in playlist
  index: number = 0;

  // current playback time (secs)
  _time: BehaviorSubject<number> = new BehaviorSubject(0);

  // duration of current song (secs)
  _duration: BehaviorSubject<number> = new BehaviorSubject(0);

  // is the player playing?
  _playing: BehaviorSubject<boolean> = new BehaviorSubject(false);

  constructor(private client: APIClient, private pqs: PlayQueueService) {
    this.initSongObserver();
    this.initPlayingObserver();
    this.initDurationObserver();
    this.initTimeObserver();
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
    ).subscribe(() => {
      this._playing.next(!this.player.paused);
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

  play(playable?: Playable, index: number = 0): void {
    if (playable != null) {
      this.playlist = playable;
      this.index = index;
      this.setSong(this.playlist.songs[index]);
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
    return this.index < this.playlist.songs.length - 1;
  }

  getNext(): Song {
    if (this.pqs.hasNext()) {
      return this.pqs.next();
    } else if (this.playlistHasNext()) {
      return this.playlist.songs[++this.index];
    }
    return null;
  }

  getPrev(): Song {
    if (this.hasPrev()) {
      return this.playlist.songs[--this.index];
    }
    return null;
  }

  setSong(song: Song): void {
    // todo: record stream of previous song
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
    return this.playlist.songs.slice(this.index + 1);
  }
}
