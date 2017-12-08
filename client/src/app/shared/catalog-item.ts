import { Image } from "./image";
import { User } from "../user/user";
import { Base64Image } from "./base64-image";

/**
 * Catalog item model.
 */
export class CatalogItem {
  constructor(
    public id: number,
    public name: string,
    public createdDate: any,
    public active: boolean,
    public image: Image | Base64Image,
    public owner: User
  ) {}
}
