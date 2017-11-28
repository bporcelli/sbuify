/**
 * Model for playlist folders.
 */
export class PlaylistFolder {
  constructor(
    public id: number,
    public name: string,
    public position: number,
    public parentFolder: PlaylistFolder
  ) {}
}
