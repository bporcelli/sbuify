import { Song } from "../songs/song";

export interface Queueable {
  getSongs(): Array<Song>;
}
