import { Song } from "../songs/song";

/**
 * Represents a song in a playlist.
 */
export class PlaylistSong {
  constructor(
    public song: Song,
    public dateSaved: any
  ) {}
}
