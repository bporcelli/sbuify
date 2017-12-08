import { Song } from "../songs/song";
import { Playlist } from "../playlist/playlist";

export class TimeRange {
  constructor(
    public start: number,
    public end: number
  ) {}
}

export class Stream {
  constructor(
    public song: Song,
    public played: TimeRange[],
    public playlist?: Playlist
  ) {}
}
