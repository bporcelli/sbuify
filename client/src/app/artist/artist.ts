import { CatalogItem } from "../shared/catalog-item";
import { Image } from "../shared/image";
import { Biography } from "./biography";
import { User } from "../user/user";

/**
 * Represents an artist.
 */
export class Artist extends CatalogItem {
  constructor(
    public id: number,
    public name: string,
    public createdDate: any,
    public active: boolean,
    public image: Image,
    public owner: User,
    public mbid: string,
    public aliases: Array<string>,
    public monthlyListeners: number,
    public bio: Biography,
    public coverImage: Image,
    public popularSongs: any
  ) {
    super(id, name, createdDate, active, image, owner);
  }
}
