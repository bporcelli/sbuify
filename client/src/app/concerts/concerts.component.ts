import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from "@angular/common/http";
import { Config } from "../config";

@Component({
  template: `
    <h2 class="h5 playlist-list-title">Concerts</h2>
    <concerts-table [concerts]="concerts" [loading]="loading"></concerts-table>
  `
})
export class ConcertsComponent implements OnInit {

  /** Concerts to display */
  concerts: any[] = [];

  /** Are the concerts loading? */
  loading: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private client: HttpClient
  ) {}

  ngOnInit(): void {
    this.loading = true;

    this.route.data
      .subscribe((data: { location: any }) => {
        this.loadConcertsForCoords(data.location.coords);
      });
  }

  private loadConcertsForCoords(coords: Coordinates) {
    let endpoint = 'http://api.songkick.com/api/3.0/events.json?apikey=' + Config.SONGKICK_API_KEY +
                   '&location=geo:' + coords.latitude + ',' + coords.longitude;

    this.client.get<any>(endpoint)
      .subscribe((response: any) => {
        let results = response.resultsPage.results.events || [];
        this.concerts = results;
        this.loading = false;
      });
  }
}
