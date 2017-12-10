import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Artist } from "./artist";
import { ArtistService } from "./artist.service";
import {Config} from "../config";

@Component({
  template: `
    <h2 class="h4">Upcoming concerts for {{ artist?.name }}</h2>
    <concerts-table [concerts]="concerts" [loading]="loading"></concerts-table>
  `
})
export class ArtistConcertsComponent implements OnInit {

  /** Artist */
  artist: Artist = null;

  /** Upcoming concerts retrieved from Songkick */
  concerts: any[] = [];

  /** Are the concerts loading? */
  loading: boolean = true;

  constructor(
    private artistService: ArtistService,
    private client: HttpClient
  ) {}

  ngOnInit(): void {
    this.artist = this.artistService.current;

    let endpoint = 'http://api.songkick.com/api/3.0/artists/mbid:' + this.artist.mbid +
                   '/calendar.json?apikey=' + Config.SONGKICK_API_KEY;

    this.loading = true;

    this.client.get<object>(endpoint)
      .subscribe((concerts: any) => {
        let results = concerts.resultsPage.results.event || [];
        this.concerts = results;
        this.loading = false;
      });
  }
}
