import { Song } from "../songs/song";
import { Playable } from "../player/playable";

export class PlayQueue implements Playable {
  constructor(public id: number,
              public songs: Array<Song>) {}
}
