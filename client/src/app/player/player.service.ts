import { Injectable } from '@angular/core';
import { Song } from "../songs/song";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { PlayQueue } from "./play-queue";
import { Queueable } from "./queueable";
import { APIClient } from "../api/api-client.service";
import { tokenGetter } from "../auth/helpers";
import { Observable } from 'rxjs/Observable';

@Injectable()
export class PlayerService {

  // audio player
  player: any = new Audio();

  // current song
  song: BehaviorSubject<Song> = new BehaviorSubject(null);

  // current playback time (secs)
  _time: BehaviorSubject<number> = new BehaviorSubject(0);

  // duration of current song (secs)
  _duration: BehaviorSubject<number> = new BehaviorSubject(0);

  // is the player playing?
  _playing: BehaviorSubject<boolean> = new BehaviorSubject(false);

  // songs up next
  private queue: Array<Song> = [];

  constructor(private client: APIClient) {
    this.initPlayQueue();
    this.initObservers();
  }

  initPlayQueue(): void {
    this.client.get<PlayQueue>("/api/customer/play-queue").subscribe(
      (resp: PlayQueue) => {
        this.queue = resp.songs;
        this.next();
      },
      (err) => {
        // todo: display error message
        console.log('error fetching play queue:', err);
      }
    );
  }

  initObservers(): void {
    this.initPlayingObserver();
    this.initDurationObserver();
    this.initTimeObserver();
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

  play(item?: Queueable): void {
    if (item != null) {  // new song or album is being played
      this.enqueue(item, true);
      this.next();
    }
    this.player.play();
    // todo: record stream
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

  next(): void {
    if (this.hasNext()) {
      let song: Song = this.queue.shift();

      this.song.next(song);
      this.player.src = '/api/stream/' + song.id + '/?token=' + tokenGetter()();
    }
  }

  hasNext(): boolean {
    return this.queue.length > 0;
  }

  prev(): void {
    // todo
  }

  hasPrev(): boolean {
    return false;
  }

  enqueue(item: Queueable, front = false) {
    if (front) {
      this.queue = item.getSongs().concat(this.queue);
    } else {
      this.queue = this.queue.concat(item.getSongs());
    }
    // todo: sync with server side
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
}
