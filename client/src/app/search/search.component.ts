import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { SearchService } from "./search.service";
import { NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { APIClient } from "../api/api-client.service";

@Component({
  templateUrl: './search.component.html',
})
export class SearchComponent implements OnInit, OnDestroy {

  // entity type being searched ('song', 'album', 'artist', 'playlist', or 'profile')
  private entityType: string = "song";

  // max results per page
  private limit: number = 25;

  // offset to first result on current page
  private offset: number = 0;

  // current search results
  private results: BehaviorSubject<any> = new BehaviorSubject([]);

  // current subscription to observable data service
  private subscription: any = null;

  constructor(private service: SearchService,
              private client: APIClient) {
  }

  ngOnInit() {
    let that = this;
    let timer: any = 0;
    let delay = 200;  // delay between searches (ms)
    this.subscription =
      this.service
      .getQuery()
      .subscribe((value: string) => {
        if (value == "") {
          this.reset();
        } else {
          clearTimeout(timer);
          timer = setTimeout(() => {
            that.start();
          }, delay);
        }
      });
  }

  ngOnDestroy(): void {
    this.service.setQuery("");
    this.subscription.unsubscribe();
  }

  private reset() {
    this.offset = 0;
    this.results = new BehaviorSubject([]);
  }

  private start() {
    this.reset();
    this.next();
  }

  private next(query?: string) {
    if (this.offset == -1) { // all results found
      return;
    }
    if (query == null) {  // reuse old query
      query = this.query;
    }

    let params: HttpParams = new HttpParams();

    params = params.set('query', query);
    params = params.set('type', this.entityType);
    params = params.set('limit', this.limit.toString());
    params = params.set('offset', this.offset.toString());

    this.client.get<any[]>("/api/search", { params: params }).subscribe(
      response =>
      {
        // notify subscribers of the new results
        this.results.next(this.results.getValue().concat(response));
        // terminate search if no new results are available
        if (response.length < this.limit) {
          this.offset = -1;
        } else {
          this.offset += this.limit;
        }
      },
      error =>
      {
        // todo: better error handling
        console.log('search error:', error);
      });
  }

  onTabChange(e: NgbTabChangeEvent) {
    this.entityType = e.nextId;

    // restart search with new entity type
    this.start();
  }

  get query(): string {
    return this.service.getQuery().getValue();
  }
}
