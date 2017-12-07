import 'rxjs/operators/map';

import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs/Rx';
import { APIClient } from "../api/api-client.service";

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

  create(playlist: object): Observable<object> {
    let endpoint: string;

    if (playlist['folder']) {
      endpoint = '/api/playlist-folders';
    } else {
      endpoint = '/api/playlists';
    }

    return this.client.post<object>(endpoint, playlist)
      .map((response: object) => {
        this._playlists.value.push(response);
        this._playlists.next(this._playlists.value);
        return response;
      });
  }

  /** Delete a playlist or folder. */
  delete(item: any) {
    let endpoint: string;

    if (item.folder) {
      endpoint = '/api/playlist-folders/' + item.id;
    } else {
      endpoint = '/api/playlists/' + item.id;
    }

    return this.client.delete(endpoint)
      .map(() => {
        // remove playlist/folder from local cache on success
        let playlists = this._playlists.value;
        for (let i = 0; i < playlists.length; i++) {
          if (item.id == playlists[i].id) {
            playlists.splice(i, 1);
            break;
          }
        }
        this._playlists.next(playlists);
      });
  }

  /** Get the playlists in a folder. */
  getFolder(id: any): Observable<object[]> {
    return this.client.get<object[]>('/api/playlist-folders/' + id);
  }

  private initPlaylists(playlists: any[]): void {
    this._playlists.next(playlists);
  }

  private handleError(err: any): void {
    // todo: show error
    console.log('error occurred while fetching playlists:', err);
  }
}
