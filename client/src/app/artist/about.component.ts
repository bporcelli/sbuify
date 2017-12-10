import { Component, OnInit } from '@angular/core';
import { Artist } from "./artist";
import { Biography } from "./biography";
import { ArtistService } from "./artist.service";

@Component({
  templateUrl: './about.component.html',
})
export class AboutComponent implements OnInit {

  /** Artist being displayed */
  artist: Artist = null;

  /** Bio */
  biography: Biography = null;

  /** Is the biography loading? */
  loading: boolean = true;

  constructor(private artistService: ArtistService) {}

  ngOnInit(): void {
    this.artist = this.artistService.current;

    this.artistService.getBiography(this.artist.id)
      .subscribe((bio) => {
        this.biography = bio;
        this.loading = false;
      });
  }
}
