import { Component, OnInit } from '@angular/core';
import { ArtistService } from "./artist.service";
import { Artist } from "./artist";

@Component({
  template: `
    <div class="page-section" artist-grid [artists]="related" [pending]="pending"></div>
    <div class="results-spinner mt-3" [hidden]="!pending">
      <i class="fa fa-spinner fa-spin"></i>
      <span>Loading...</span>
    </div>
  `,
})
export class RelatedArtistsComponent implements OnInit {

  /** List of related artists */
  related: Artist[] = [];

  /** Is our request for the artist's related artists pending? */
  pending: boolean = true;

  constructor(private artistService: ArtistService) {}

  ngOnInit(): void {
    let artist = this.artistService.current;

    this.pending = true;

    this.artistService.getRelated(artist.id, 18)
      .subscribe((related) => {
        this.related = related;
        this.pending = false;
      });
  }
}
