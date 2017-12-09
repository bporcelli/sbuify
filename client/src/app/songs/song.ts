import { CatalogItem } from "../shared/catalog-item";
import { Image } from "../shared/image";
import { Album } from "../album/album";
import { User } from "../user/user";
import { Queueable } from "../play-queue/queueable";
import { Playable } from "../playback/playable";

/**
 * Represents a song in the music catalog.
 */
export class Song extends CatalogItem implements Queueable, Playable {
  public type: string = 'song';

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
