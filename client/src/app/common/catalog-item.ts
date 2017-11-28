import { Image } from "./image";
import { User } from "../user/user";

/**
 * Catalog item model.
 */
export class CatalogItem {
  constructor(
    public id: number,
    public name: string,
    public createdDate: any,
    public active: boolean,
    public image: Image,
    public owner: User
  ) {}
}
