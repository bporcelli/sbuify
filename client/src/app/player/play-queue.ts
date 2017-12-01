import { Song } from "../songs/song";

export class PlayQueue {
  constructor(public id: number,
              public songs: Array<Song>) {}
}
