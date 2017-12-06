import { Song } from "./song";
import { Playable } from "../player/playable";

/**
 * A playable list of songs.
 */
let nextId: number = 0;

export class SongList implements Playable {
  public id: number = 0;

  constructor(public songs: Array<Song>, id: any = null) {
    if (id == null) {
      this.id = nextId++;
    } else {
      this.id = id;
    }
  }
}
