import { Song } from "../songs/song";
import { Playable } from "../playback/playable";

export class PlayQueue implements Playable {
  type: string;

  constructor(
    public id: number,
    public songs: Array<Song>
  ) {}
}
