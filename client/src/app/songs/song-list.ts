import { Song } from "./song";
import { Playable } from "../player/playable";

/**
 * A playable list of songs.
 */
let id: number = 0;

export class SongList implements Playable {
  constructor(public songs: Array<Song>) {}

  get id(): number {
    return id++;
  }
}
