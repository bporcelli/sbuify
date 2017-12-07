import 'rxjs/operators/map';

import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs/Rx';
import { APIClient } from "../api/api-client.service";
import { Playlist } from "./playlist";

@Injectable()
export class PlaylistService {

  private _playlists: BehaviorSubject<any[]> = new BehaviorSubject([]);

  constructor(private client: APIClient) {
    this.client.get<any[]>('/api/customer/playlists')
      .subscribe(
        (resp: any[]) => this.initPlaylists(resp),
        (err: any) => this.handleError(err)
      );
  }

  getAll(): Observable<any[]> {
    return this._playlists;
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

  private initPlaylists(playlists: any[]): void {
    console.log('got playlists from server:');
    console.log(playlists);

    this._playlists.next(playlists);
  }

  private handleError(err: any): void {
    // todo: show error
    console.log('error occurred while fetching playlists:', err);
  }
}
