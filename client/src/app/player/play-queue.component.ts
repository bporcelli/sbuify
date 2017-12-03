import { Component } from '@angular/core';
import { PlayQueueService } from "./play-queue.service";
import { PlayerService } from "./player.service";
import { Song } from "../songs/song";
import { Observable } from "rxjs/Observable";
import { PlayQueue } from "./play-queue";
import { SongList } from "./song-list";

@Component({
    templateUrl: './play-queue.component.html',
})
export class PlayQueueComponent {
  constructor(private pqs: PlayQueueService,
              private ps: PlayerService) {}

  get current(): Observable<Array<Song>> {
    return this.ps.song.switchMap((song: Song) => {
      return Observable.of([song]);
    });
  }

  get queued(): Observable<Array<Song>> {
    return this.pqs.get().switchMap((pq: PlayQueue) => {
      return Observable.of(pq.songs);
    });
  }

  get playlist(): SongList {
    return this.ps.playlist;
  }
}
