import 'rxjs/operators/map';

import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs/Rx';
import { APIClient } from "../shared/api-client.service";
import { PlaylistSong } from "./playlist-song";
import { Playlist } from "./playlist";
import { FollowingService } from "../user/following.service";

@Injectable()
export class PlaylistService {

  /** Flattened map of playlists */
  private playlists: object = {};

  /** Playlist folder structure */
  private folders: object = {
    0: new BehaviorSubject(null)
  };

  constructor(
    private client: APIClient,
    private followingService: FollowingService
  ) {
    this.client.get<any[]>('/api/customer/playlists')
      .subscribe(
        (resp: any[]) => this.save(resp),
        (err: any) => this.handleError(err)
      );
  }

  /** Get a playlist from the local cache or server */
  getPlaylist(id: number | string): Observable<object> {
    if (id in this.playlists) {
      return Observable.of(this.playlists[id]);
    }
    return this.client.get<object>('/api/playlists/' + id)
      .map((playlist: object) => {
        this.save(playlist);
        return playlist;
      });
  }

  /** Add a new playlist or list of playlists to the local cache */
  private save(toAdd: object | object[]) {
    let playlists: object[];

    if (!Array.isArray(toAdd)) {
      playlists = [toAdd];
    } else {
      playlists = <object[]>toAdd;
    }

    for (let playlist of playlists) {
      this.playlists[playlist['id']] = playlist;

      let folder = playlist['parent'] ? playlist['parent']['id'] : 0;

      if (!(folder in this.folders)) {
        this.folders[folder] = new BehaviorSubject([]);
      }
      this.addToFolder(folder, playlist);
    }
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
      .map((playlist: object) => {
        this.save(playlist);
        return playlist;
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
          (playlists: object[]) => this.save(playlists),
          (err: any) => this.handleError(err)
        );
    }
    return this.folders[id];
  }

  /** Get the songs in a playlist */
  getSongs(id: number, page: number): Observable<PlaylistSong[]> {
    let params = new HttpParams();
    params = params.set("page", page.toString());
    return this.client.get<PlaylistSong[]>('/api/playlists/' + id + '/songs', { params: params });
  }

  /** Follow or unfollow a playlist */
  followOrUnfollow(playlist: Playlist): void {
    if (playlist['isFollowed']) {
      this.followingService.unfollow('playlist', playlist.id)
        .subscribe(() => {
          playlist['isFollowed'] = false;
          this.removeFromFolder(0, playlist);
        });
    } else {
      this.followingService.follow('playlist', playlist.id)
        .subscribe(() => {
          playlist['isFollowed'] = true;
          this.addToFolder(0, playlist);
        });
    }
  }

  private handleError(err: any): void {
    // todo: show error
    console.log('error occurred while fetching playlists:', err);
  }

  private addToFolder(id: number, playlist: object): void {
    let playlists = this.folders[id].value;

    if (playlists == null) {
      playlists = [playlist];
    } else if (this.findPlaylist(playlist['id'], playlists) == -1) {
      playlists.push(playlist);
    }
    this.folders[id].next(playlists);
  }

  private removeFromFolder(id: number, playlist: object): void {
    let playlists = this.folders[id].value;

    if (playlists == null) {
      return;
    }

    let index = this.findPlaylist(playlist['id'], playlists);
    if (index >= 0) {
      playlists.splice(index, 1);
    }
    this.folders[id].next(playlists);
  }
}
