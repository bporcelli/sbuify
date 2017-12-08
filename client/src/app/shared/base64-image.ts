export class Base64Image {
  /** Type information, required for deserialization */
  public type = 'base64_image';

  constructor(public dataURL: string) {}
}
