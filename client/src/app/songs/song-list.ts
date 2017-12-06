import { Song } from "./song";
import { Playable } from "../player/playable";

/**
 * A playable list of songs.
 */
let id: number = 0;

export class SongList implements Playable {
  public id: number = 0;

  constructor(public songs: Array<Song>) {
    this.id = id++;
  }
}
