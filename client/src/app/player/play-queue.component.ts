import { Component } from '@angular/core';
import { PlayQueueService } from "./play-queue.service";
import { PlayerService } from "./player.service";
import { Song } from "../songs/song";
import { Observable } from "rxjs/Rx";
import { PlayQueue } from "./play-queue";
import { Playable } from "./playable";
import { SongList } from "../songs/song-list";

@Component({
    templateUrl: './play-queue.component.html',
})
export class PlayQueueComponent {
  constructor(private pqs: PlayQueueService,
              private ps: PlayerService) {}

  get current(): Observable<Playable> {
    return this.ps.song.switchMap((song: Song) => {
      if (song == null) {
        return Observable.of(null);
      }
      return Observable.of(new SongList([song]));
    });
  }

  get queued(): Observable<Playable> {
    return this.pqs.get().map((pq: PlayQueue) => {
      if (pq == null) return;
      return new PlayQueue(pq.id, pq.songs);
    });
  }

  hasNext(): boolean {
    return this.ps.playlistHasNext();
  }

  get playlist(): Playable {
    return this.ps.playlist;
  }

  get upcoming(): Array<Song> {
    return this.ps.upcoming;
  }

  get songIndex(): number {
    return this.ps.index;
  }
}
