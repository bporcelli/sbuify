import { CatalogItem } from "../shared/catalog-item";
import { Image } from "../shared/image";
import { PlaylistSong } from "./playlist-song";
import { Customer } from "../user/customer";
import { Playable } from "../playback/playable";
import { Song } from "../songs/song";
import { Base64Image } from "../shared/base64-image";
import { SongList } from "../songs/song-list";

/**
 * Playlist model.
 */
export class Playlist extends CatalogItem implements Playable, SongList {
  public type: string = 'playlist';

  constructor(
    public id: number,
    public name: string,
    public createdDate: any,
    public active: boolean,
    public image: Image | Base64Image,
    public owner: Customer,
    public description: string,
    public hidden: boolean,
    public length: number,
    public numSongs: number,
    private _songs: Array<PlaylistSong>
  ) {
    super(id, name, createdDate, active, image, owner);
  }

  get songs(): Array<Song> {
    return this._songs.map(ps => ps.song);
  }
}
