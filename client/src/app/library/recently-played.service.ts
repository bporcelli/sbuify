import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from "rxjs/Rx";
import { APIClient } from "../shared/api-client.service";

@Injectable()
export class RecentlyPlayedService {

  constructor(private client: APIClient) {}

  /** Get a page of recently played songs, artists, albums, or playlists. */
  public getPage(page: number, type: string = ''): Observable<any[]> {
    let endpoint = '/api/customer/recently-played';

    if (type != 'songs') {
      endpoint += '/' + type;
    }

    let params = new HttpParams().set("page", page.toString());

    return this.client.get<any[]>(endpoint, { params: params });
  }
}
