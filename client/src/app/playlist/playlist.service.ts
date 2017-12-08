import 'rxjs/operators/map';

import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs/Rx';
import { APIClient } from "../common/api-client.service";

@Injectable()
export class PlaylistService {

  private folders: object = {
    0: new BehaviorSubject(null)
  };

  constructor(private client: APIClient) {
    this.client.get<any[]>('/api/customer/playlists')
      .subscribe(
        (resp: any[]) => this.folders[0].next(resp),
        (err: any) => this.handleError(err)
      );
  }

  getPlaylist(id: number | string): Observable<object> {
    // todo: get from server
    return null;
  }

  /** Create a playlist or folder. */
  create(playlist: object, folder: object = null): Observable<object> {
    let endpoint: string;

    if (playlist['folder']) {
      endpoint = '/api/playlist-folders';
    } else {
      endpoint = '/api/playlists';
    }

    let params = new HttpParams();

    if (folder != null) {
      params = params.set("folderId", folder['id']);
    }

    return this.client.post<object>(endpoint, playlist, { params: params })
      .map((response: object) => {
        let fID = folder ? folder['id'] : 0;

        this.folders[fID].value.push(response);
        this.folders[fID].next(this.folders[fID].value);

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

    return this.client.patch<object>(endpoint, playlist)
      .map((updated: object) => {
        if (playlist['folder']) {
          this.syncFolder(updated);
        } else {
          this.syncPlaylist(updated);
        }
      });
  }

  /** Sync local playlist with server copy */
  private syncPlaylist(updated: object): void {
    for (let fID in this.folders) {
      let folder = this.folders[fID];
      let playlists = folder.value;
      let index = this.findPlaylist(updated['id'], playlists);

      if (index >= 0) {
        playlists[index] = updated;
        this.folders[fID].next(playlists);
        break;
      }
    }
  }

  /** Sync local folder with server copy */
  private syncFolder(updated: object): void {
    let index = this.findPlaylist(updated['id']);
    let playlists = this.folders[0].value;

    if (index >= 0) {
      playlists[index]['name'] = updated['name'];
      this.folders[0].next(playlists);
    }
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
        for (let fID in this.folders) {
          let folder = this.folders[fID];
          let playlists = folder.value;
          let index = this.findPlaylist(item.id, playlists);

          if (index >= 0) {
            playlists.splice(index, 1);
          }
          this.folders[fID].next(playlists);
        }
      });
  }

  /** Find the index of the playlist with the given ID */
  private findPlaylist(id, folder: object[] = null): number {
    let playlists = folder == null ? this.folders[0].value : folder;
    for (let i = 0; i < playlists.length; i++) {
      if (id == playlists[i].id)
        return i;
    }
    return -1;
  }

  /** Get the playlists in a folder. */
  getFolder(id: any): Observable<object[]> {
    if (!(id in this.folders)) {
      this.folders[id] = new BehaviorSubject(null);
      this.client.get<object[]>('/api/playlist-folders/' + id)
        .subscribe(
          (playlists: object[]) => this.folders[id].next(playlists),
          (err: any) => this.handleError(err)
        );
    }
    return this.folders[id];
  }

  private handleError(err: any): void {
    // todo: show error
    console.log('error occurred while fetching playlists:', err);
  }
}
