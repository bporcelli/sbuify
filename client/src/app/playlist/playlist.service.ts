import 'rxjs/operators/map';

import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs/Rx';
import { APIClient } from "../api/api-client.service";
import { Playlist } from "./playlist";

@Injectable()
export class PlaylistService {

  private _playlists: BehaviorSubject<Playlist[]> = new BehaviorSubject([]);

  constructor(private client: APIClient) {}

  getAll(): Observable<Playlist[]> {
    // todo: concact server for playlists; return PlaylistComponent[] instead of Playlist[]
    return Observable.of([]);
  }

  getPlaylist(id: number | string) {
    return this.getAll()
      // (+) converts id to string
      .map(playlists => playlists.find(playlist => playlist.id == +id));
  }

  create(playlist: object): Observable<Playlist> {
    return this.client.post<Playlist>('/api/playlists', playlist)
      .map((response: Playlist) => {
        this._playlists.value.push(response);
        this._playlists.next(this._playlists.value);
        return response;
      });
  }
}
