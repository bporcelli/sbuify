import { Component, OnInit } from '@angular/core';
import { Album } from "./album";
import { LibraryService } from "../user/library.service";
import { Config } from "../config";
import { BehaviorSubject } from "rxjs/Rx";

@Component({
  templateUrl: './albums.component.html'
})
export class AlbumsComponent implements OnInit {

  /** Current page */
  private page: number = 0;

  /** Current filter */
  private filter: string = '';

  /** Unfiltered albums */
  private unfiltered: Album[] = [];

  /** Filtered albums */
  private _albums: BehaviorSubject<Album[]> = new BehaviorSubject([]);

  /** Is the request for the next page of results pending? */
  private pending: boolean = false;

  /** Are more albums available? */
  private more: boolean = true;

  constructor(private libService: LibraryService) {}

  ngOnInit(): void {
    this.nextPage();
  }

  private nextPage() {
    if (this.pending) {  // request in flight
      return;
    } else if (!this.more) {  // all albums retrieved
      return;
    }

    this.pending = true;

    this.libService.getAlbums(this.page)
      .subscribe(
        (resp: Album[]) => this.handleNewAlbums(resp),
        (err: any) => this.handleError(err)
      );
  }

  private handleNewAlbums(albums: Album[]) {
    this.pending = false;

    this.unfiltered.push(...albums);
    this.onFilterChange();
    this.page += 1;

    if (albums.length < Config.ITEMS_PER_PAGE) {
      this.more = false;
    }
  }

  private handleError(err: any) {
    // todo: display error
    console.log('error encountered while fetching next page of albums:', err);
  }

  private onFilterChange(value: string = null) {
    if (value == null) {
      value = this.filter;
    }

    value = value.toLowerCase();

    const temp = this.unfiltered.filter((album: Album) => {
      return album.name.toLowerCase().indexOf(value) !== -1 || !value;
    });

    this._albums.next(temp);
    this.filter = value;
  }

  get albums(): BehaviorSubject<Album[]> {
    return this._albums;
  }
}
