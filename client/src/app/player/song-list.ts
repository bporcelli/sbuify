import { Song } from "../songs/song";

/**
 * Represents a list of songs navigable using "next" and "previous" buttons.
 */
export class SongList {
  private _index: number;
  private _songs: Array<Song>;

  constructor(songs: Array<Song>, index: number = -1) {
    this._songs = songs;
    this._index = index;
  }

  next(): Song {
    if (this.hasNext()) {
      return this._songs[++this._index];
    }
    return null;
  }

  prev(): Song {
    if (this.hasPrev()) {
      return this._songs[--this._index];
    }
    return null;
  }

  hasNext(): boolean {
    return this._index < this._songs.length - 1;
  }

  hasPrev(): boolean {
    return this._index > 0;
  }

  get songs(): Array<Song> {
    return this._songs;
  }

  get upcoming(): Array<Song> {
    return this._songs.slice(this._index + 1);
  }

  get index(): number {
    return this._index;
  }
}
