import { Component, Input } from '@angular/core';
import { Song } from "./song";

@Component({
  selector: '[song-table]',
  templateUrl: './song-table.component.html'
})
export class SongTableComponent {
  @Input() songs: Array<Song> = [];
}
