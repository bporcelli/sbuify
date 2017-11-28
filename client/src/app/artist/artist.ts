import { CatalogItem } from "../common/catalog-item";
import { Image } from "../common/image";
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
    public coverImage: Image
  ) {
    super(id, name, createdDate, active, image, owner);
  }
}
