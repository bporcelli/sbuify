import { Injectable } from '@angular/core';
import { PlayQueue } from "./play-queue";
import { APIClient } from "../shared/api-client.service";
import { Queueable } from "./queueable";
import { Song } from "../songs/song";
import { Observable } from "rxjs/Rx";
import { BehaviorSubject } from "rxjs/BehaviorSubject";

@Injectable()
export class PlayQueueService {

  private _queue: BehaviorSubject<PlayQueue> = new BehaviorSubject(null);

  constructor(private client: APIClient) {
    this.client.get<PlayQueue>("/api/customer/play-queue").subscribe(
      (response: PlayQueue) => {
        this._queue.next(response);
      },
      (err: any) => {
        console.log('failed to fetch play queue from server:', err);
      }
    );
  }

  get(): Observable<PlayQueue> {
    return this._queue;
  }

  add(item: Queueable): void {
    this.client.post("/api/customer/play-queue/add", item, { responseType: 'text' }).subscribe(
      () => {
        let songs = item['type'] == 'song' ? [<Song>item] : item.songs;

        // set queued flag for all songs
        songs.forEach((song: Song) => song['queued'] = true);

        this.queue.songs = this.queue.songs.concat(songs);
        this._queue.next(this.queue);
      },
      (err: any) => {
        // todo: show error message
        console.log('error adding', item, 'to play queue:', err);
      }
    );
  }

  remove(song: Song): void {
    this.client.post("/api/customer/play-queue/remove", song, { responseType: 'text' }).subscribe(
      () => {
        let index: number = -1;

        for (let i = 0; i < this.queue.songs.length; i++) {
          if (this.queue.songs[i].id == song.id) {
            index = i;
            break;
          }
        }

        if (index != -1) {
          this.queue.songs.splice(index, 1);
          this._queue.next(this.queue);

          // unset queued flag
          song['queued'] = false;
        }
      },
      (err: any) => {
        console.log('error removing', song, 'from play queue:', err);
      }
    );
  }

  /** Skip all songs up to and including the song at the provided index. */
  skip(index: number) {
    let skipped = this.queue.songs.splice(0, index + 1);

    // unset queued flag for skipped songs
    skipped.forEach(song => song['queued'] = false);

    this.sync();
  }

  hasNext(): boolean {
    if (this.queue == null)
      return false;
    return this.queue.songs.length > 0;
  }

  next(): Song {
    if (this.hasNext()) {
      let next = this.queue.songs.shift();
      next['queued'] = false;
      this.sync();
      return next;
    }
    return null;
  }

  get queue(): PlayQueue {
    return this._queue.getValue();
  }

  /** Sync the play queue with the server */
  private sync() {
    this.client.put("/api/customer/play-queue", this.queue, { responseType: 'text' }).subscribe(
      () => {
        this._queue.next(this.queue);
      },
      (err: any) => {
        console.log('failed to update play queue. error was:', err);
      }
    );
  }
}
