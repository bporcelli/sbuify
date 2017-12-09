import { Song } from "../songs/song";

export interface Playable {
  id: any;
  songs: Array<Song>;
  type: string;
}
