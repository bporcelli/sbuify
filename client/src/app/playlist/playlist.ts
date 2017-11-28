import { CatalogItem } from "../common/catalog-item";
import { Image } from "../common/image";
import { PlaylistFolder } from "./playlist-folder";
import { PlaylistSong } from "./playlist-song";
import { Customer } from "../user/customer";

/**
 * Playlist model.
 */
export class Playlist extends CatalogItem {
  constructor(
    public id: number,
    public name: string,
    public createdDate: any,
    public active: boolean,
    public image: Image,
    public owner: Customer,
    public position: number,
    public parentFolder: PlaylistFolder,
    public description: string,
    public hidden: boolean,
    public songs: Array<PlaylistSong>
  ) {
    super(id, name, createdDate, active, image, owner);
  }

  get length(): number {
    return this.songs.map((x: PlaylistSong) => x.song.length).reduce((acc, curVal) => acc + curVal);
  }
}
