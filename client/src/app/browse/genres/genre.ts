import { Image } from "../../common/image";

/**
 * Represents a musical genre.
 */
export class Genre {
  constructor(
    public id: number,
    public name: string,
    public image: Image
  ) {}
}
