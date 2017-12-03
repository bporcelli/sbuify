import { CatalogItem } from "../common/catalog-item";
import { Image } from "../common/image";
import { Album } from "../album/album";
import { User } from "../user/user";
import { Queueable } from "../player/queueable";
import { Playable } from "../player/playable";

/**
 * Represents a song in the music catalog.
 */
export class Song extends CatalogItem implements Queueable, Playable {
  constructor(
    public id: number,
    public name: string,
    public createdDate: any,
    public active: boolean,
    public image: Image,
    public owner: User,
    public length: number,
    public trackNumber: number,
    public playCount: number,
    public mbid: string,
    public lyrics: string,
    public album: Album
  ) {
    super(id, name, createdDate, active, image, owner);
  }

  get songs(): Array<Song> {
    return [this];
  }
}
