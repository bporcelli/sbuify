import { CatalogItem } from "../shared/catalog-item";
import { Image } from "../shared/image";
import { Song } from "../songs/song";
import { Artist } from "../artist/artist";
import { User } from "../user/user";
import { Queueable } from "../play-queue/queueable";
import { Playable } from "../playback/playable";

/**
 * Represents an album.
 */
export class Album extends CatalogItem implements Queueable, Playable {
  constructor(
    public id: number,
    public name: string,
    public createdDate: any,
    public active: boolean,
    public image: Image,
    public owner: User,
    public releaseDate: any,
    public length: number,
    public numSongs: number,
    public mbid: string,
    public artist: Artist,
    public type: string,
    public songs: Array<Song>
  ) {
    super(id, name, createdDate, active, image, owner);
  }
}
