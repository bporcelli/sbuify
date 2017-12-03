import { Injectable } from '@angular/core';
import { PlayQueue } from "./play-queue";
import { APIClient } from "../api/api-client.service";
import { Queueable } from "./queueable";
import { Song } from "../songs/song";
import { Observable } from "rxjs/Observable";

@Injectable()
export class PlayQueueService {

  private queue: PlayQueue = null;

  constructor(private client: APIClient) {}

  get(): Observable<PlayQueue> {
    if (this.queue != null) {
      return Observable.of(this.queue);
    }
    return this.client.get<PlayQueue>("/api/customer/play-queue").switchMap(
      (response: PlayQueue) => {
        this.queue = response;
        return Observable.of(response);
      }
    );
  }

  add(item: Queueable): void {
    // todo: add and sync with server
  }

  remove(song: Song): void {
    // todo: remove and sync with server
  }

  hasNext(): boolean {
    return this.queue.songs.length > 0;
  }

  next(): Song {
    if (this.hasNext()) {
      return this.queue.songs.shift();
    }
    return null;
  }
}
