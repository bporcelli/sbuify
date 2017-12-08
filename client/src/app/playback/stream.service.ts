import { Injectable } from '@angular/core';
import { Song } from "../songs/song";
import { Stream, TimeRange } from "./stream";
import { Playlist } from "../playlist/playlist";
import { APIClient } from "../shared/api-client.service";

@Injectable()
export class StreamService {
  constructor(private client: APIClient) {}

  /** Record a stream of a particular song */
  create(song: Song, ranges: TimeRanges, playlist: Playlist = null) {
    // ensure type id is set
    if (!('type' in song)) {
      song['type'] = 'song';
    }

    // build and record stream
    let played: TimeRange[] = this.timeRangesToArray(ranges);
    let stream = new Stream(song, played, playlist);

    this.client.post<Stream>('/api/customer/streams', stream)
      .subscribe(
        () => // success,
        (err: any) => this.handleError(err)
      );
  }

  private timeRangesToArray(ranges: TimeRanges): TimeRange[] {
    let played: TimeRange[] = [];
    for (let i = 0; i < ranges.length; i++) {
      played.push(new TimeRange(ranges.start(i), ranges.end(i)));
    }
    return played;
  }

  private handleError(err: any): void {
    console.log('error occurred while recording stream:', err);
  }
}
