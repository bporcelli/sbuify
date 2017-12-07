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

  /** Create a playlist or folder. */
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

  /** Update a playlist. */
  update(playlist: object): Observable<void> {
    let endpoint: string;

    if (playlist['folder']) {
      endpoint = '/api/playlist-folders/' + playlist['id'];
    } else {
      endpoint = '/api/playlists/' + playlist['id'];
    }

    return this.client.patch<void>(endpoint, playlist)
      .map(() => {
        if (playlist['folder']) {
          this.syncFolder(playlist);
        } else {
          this.syncPlaylist(playlist);
        }
      });
  }

  /** Sync local playlist with server copy */
  private syncPlaylist(playlist: object): void {
    let index = this.findPlaylist(playlist['id']);
    let playlists = this._playlists.value;

    if (index >= 0) {
      playlists[index]['name'] = playlist['name'];
      playlists[index]['description'] = playlist['description'];
      playlists[index]['hidden'] = playlist['hidden'];

      if (playlist['image']['type'] == 'base64_image') {  // image changed
        playlists[index]['image'] = playlist['image'];
      }
    }

    this._playlists.next(playlists);
  }

  /** Sync local folder with server copy */
  private syncFolder(folder: object): void {
    let index = this.findPlaylist(folder['id']);
    let playlists = this._playlists.value;

    if (index >= 0) {
      playlists[index]['name'] = folder['name'];
    }

    this._playlists.next(playlists);
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
        // remove playlist/folder locally
        let index = this.findPlaylist(item.id);
        let playlists = this._playlists.value;

        if (index >= 0) {
          playlists.splice(index, 1);
        }
        this._playlists.next(playlists);
      });
  }

  /** Find the index of the playlist with the given ID */
  private findPlaylist(id): number {
    let playlists = this._playlists.value;

    for (let i = 0; i < playlists.length; i++) {
      if (id == playlists[i].id)
        return i;
    }
    return -1;
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
