import { Component, OnInit } from '@angular/core';
import { Artist } from "../artist/artist";
import { LibraryService } from "./library.service";
import { Config } from "../config";
import { BehaviorSubject } from "rxjs/Rx";

@Component({
  templateUrl: './artists.component.html'
})
export class ArtistsComponent implements OnInit {

  /** Current page. */
  private page: number = 0;

  /** Is there a pending request for the next page? */
  public pending: boolean = false;

  /** Are more artists available? */
  private more: boolean = true;

  /** Current filter. */
  private filter: string = '';

  /** Unfiltered artists list. */
  private unfiltered: Artist[] = [];

  /** Filtered artists. */
  private _artists: BehaviorSubject<Artist[]> = new BehaviorSubject([]);

  constructor(private libService: LibraryService) {}

  ngOnInit(): void {
    this.nextPage();
  }

  private nextPage() {
    if (this.pending) {
      return;
    } else if (!this.more) {
      return;
    }

    this.pending = true;

    this.libService.getArtists(this.page)
      .subscribe(
        (artists: Artist[]) => this.handleNewArtists(artists),
        (err: any) => this.handleError(err)
      );
  }

  private handleNewArtists(artists: Artist[]) {
    this.pending = false;

    this.unfiltered.push(...artists);
    this.onFilterChange();
    this.page += 1;

    if (artists.length < Config.ITEMS_PER_PAGE) {
      this.more = false;
    }
  }

  private handleError(err: any) {
    // todo: display error
    console.log('error occurred while fetching next page of artists:', err);
  }

  onFilterChange(value: string = null) {
    if (value == null) {
      value = this.filter;
    }

    value = value.toLowerCase();

    const temp = this.unfiltered.filter((artist: Artist) => {
      return artist.name.toLowerCase().indexOf(value) !== -1 || !value;
    });

    this._artists.next(temp);
    this.filter = value;
  }

  get artists(): BehaviorSubject<Artist[]> {
    return this._artists;
  }
}
