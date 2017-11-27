import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class SearchService {

  // current search query
  private query: BehaviorSubject<string> = new BehaviorSubject("");

  // emit the new query value to all subscribers when the query changes
  setQuery(query: string): void {
    this.query.next(query);
  }

  getQuery() {
    return this.query;
  }
}
