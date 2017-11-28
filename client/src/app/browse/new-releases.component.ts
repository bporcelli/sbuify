import { Component, OnInit } from '@angular/core';
import { Album } from "../album/album";
import { AlbumService } from "../album/album.service";
import { BehaviorSubject } from "rxjs/BehaviorSubject";

@Component({
    templateUrl: './new-releases.component.html'
})
export class NewReleasesComponent implements OnInit {

  page: number = 0;
  more: boolean = true;
  pending: boolean = false;
  albums: BehaviorSubject<Array<Album>> = new BehaviorSubject(null);

  constructor(private as: AlbumService) {}

  ngOnInit(): void {
    this.nextPage();
  }

  nextPage(): void {
    if (!this.more || this.pending) {
      return;
    }
    this.pending = true;
    this.as.getRecent(this.page).subscribe(
      (albums: Array<Album>) => {
        if (albums.length == 0) {
          this.more = false;
        }
        if (this.albums.value != null) {
          albums = this.albums.value.concat(albums);
        }
        this.albums.next(albums);
        this.pending = false;
      },
      () => {
        // todo: display error message
        this.pending = false;
      }
    );
    this.page++;
  }
}
