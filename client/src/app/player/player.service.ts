import { Injectable } from '@angular/core';
import { Song } from "../songs/song";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { PlayQueue } from "./play-queue";
import { Queueable } from "./queueable";
import { APIClient } from "../api/api-client.service";

@Injectable()
export class PlayerService {

  // current song
  song: BehaviorSubject<Song> = new BehaviorSubject(null);

  // is the player playing?
  playing: BehaviorSubject<boolean> = new BehaviorSubject(false);

  // songs up next
  private queue: Array<Song> = [];

  constructor(private client: APIClient) {
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

  play(item?: Queueable): void {
    if (item != null) {  // new song or album is being played
      this.enqueue(item, true);
      this.next();
    }
    this.playing.next(true);
    // todo: record stream
  }

  pause(): void {
    this.playing.next(false);
  }

  next(): void {
    if (this.hasNext()) {
      this.song.next(this.queue.shift());
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
}
