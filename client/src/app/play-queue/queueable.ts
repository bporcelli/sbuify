import { Song } from "../songs/song";

export interface Queueable {
  songs: Array<Song>;
}
