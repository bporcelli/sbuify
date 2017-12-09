import { Component, OnInit } from '@angular/core';
import { NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RecentlyPlayedService } from "./recently-played.service";
import { Song } from "../songs/song";
import { Artist } from "../artist/artist";
import { Album } from "../album/album";
import { Playlist } from "app/playlist/playlist";
import { Config } from "../config";

@Component({
  templateUrl: './recently-played.component.html'
})
export class RecentlyPlayedComponent implements OnInit {

  /** Selected tab ('songs', 'albums', 'artists', or 'playlists') */
  private tab: string = 'songs';

  /** Data retrieved from server */
  private recent: object = {
    'songs': {
      page: 0,
      items: [],
      more: true
    },
    'albums': {
      page: 0,
      items: [],
      more: true
    },
    'artists': {
      page: 0,
      items: [],
      more: true
    },
    'playlists': {
      page: 0,
      items: [],
      more: true
    }
  };

  /** Is a request for more results pending? */
  pending: boolean = false;

  constructor(private recentlyPlayedService: RecentlyPlayedService) {}

  ngOnInit(): void {
    this.nextPage();
  }

  onTabChange(event: NgbTabChangeEvent) {
    this.tab = event.nextId;

    if (this.recent[this.tab]['page'] == 0) {  // results for selected tab not initialized
      this.nextPage();
    }
  }

  nextPage(): void {
    let recent = this.recent[this.tab];

    if (this.pending) {
      return;
    } else if (!recent['more']) {
      return;
    }

    this.pending = true;

    this.recentlyPlayedService.getPage(recent['page'], this.tab)
      .subscribe(
        (results: any[]) => this.handleNewResults(results, this.tab),
        (err: any) => this.handleError(err)
      );
  }

  private handleNewResults(results: any[], type: string): void {
    this.pending = false;

    this.recent[type]['items'].push(...results);

    if (results.length < Config.ITEMS_PER_PAGE) {  // no more results of this type are available
      this.recent[type]['more'] = false;
    }

    this.recent[type]['page']++;
  }

  private handleError(err: any): void {
    // todo: show error
    console.log('error occurred while getting recently played:', err);
  }

  get songs(): Song[] {
    return this.recent['songs']['items'];
  }

  get artists(): Artist[] {
    return this.recent['artists']['items'];
  }

  get albums(): Album[] {
    return this.recent['albums']['items'];
  }

  get playlists(): Playlist[] {
    return this.recent['playlists']['items'];
  }
}
