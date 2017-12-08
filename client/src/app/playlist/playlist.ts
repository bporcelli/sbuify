import { CatalogItem } from "../common/catalog-item";
import { Image } from "../common/image";
import { PlaylistSong } from "./playlist-song";
import { Customer } from "../user/customer";
import { Playable } from "../playback/playable";
import { Song } from "../songs/song";
import { Base64Image } from "../common/base64-image";

/**
 * Playlist model.
 */
export class Playlist extends CatalogItem implements Playable {
  constructor(
    public id: number,
    public name: string,
    public createdDate: any,
    public active: boolean,
    public image: Image | Base64Image,
    public owner: Customer,
    public description: string,
    public hidden: boolean,
    private _songs: Array<PlaylistSong>  // todo: will this be an issue when decoding playlists?
  ) {
    super(id, name, createdDate, active, image, owner);
  }

  get length(): number {
    return this.songs.map(song => song.length).reduce((acc, curVal) => acc + curVal);
  }

  get songs(): Array<Song> {
    return this._songs.map(ps => ps.song);
  }
}
