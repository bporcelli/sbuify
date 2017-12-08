/**
 * Image model.
 */
export class Image {
  type: string = 'image';

  constructor(
    public id: number,
    public width: number,
    public height: number
  ) {}
}
