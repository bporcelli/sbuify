import { Pipe, PipeTransform } from '@angular/core';
import { Song } from "./song";
import { SongList } from "./song-list";

/**
 * Transforms an array of Song into a SongList.
 */
@Pipe({
  name: 'toSongList'
})
export class ToSongListPipe implements PipeTransform {
  transform(value: Array<Song>, ...args): any {
    return new SongList(value);
  }
}
