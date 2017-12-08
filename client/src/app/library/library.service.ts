import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { APIClient } from "../shared/api-client.service";
import { Queueable } from "../play-queue/queueable";
import { Observable } from "rxjs/Rx";
import { PlaylistSong } from "../playlist/playlist-song";
import { Album } from "../album/album";
import { Artist } from "../artist/artist";

@Injectable()
export class LibraryService {

  /** Dictionary for keeping track of saved albums and songs (initial values taken from server response) */
  private saved: object = {};

  constructor(private client: APIClient) {}

  /** Save or remove a song from the authenticated customer's library. */
  saveOrRemove(queueable: Queueable) {
    if (this.isSaved(queueable)) {
      this.remove(queueable);
    } else {
      this.save(queueable);
    }
  }

  /** Determine whether a song or album is saved. */
  isSaved(queueable: Queueable): boolean {
    let key = this.getKey(queueable);
    if (!(key in this.saved)) {
      this.saved[key] = queueable['saved'];
    }
    return this.saved[key];
  }

  private remove(queueable: Queueable) {
    let endpoint = this.getEndpoint(queueable);

    this.client.delete(endpoint)
      .subscribe(
        (resp: any) => this.setSaved(queueable, false),
        (err: any) => this.handleError('remove', err)
      );
  }

  private save(queueable: Queueable) {
    let endpoint = this.getEndpoint(queueable);

    this.client.post(endpoint, null)
      .subscribe(
        (resp: any) => this.setSaved(queueable),
        (err: any) => this.handleError('save', err)
      );
  }

  private setSaved(queueable: Queueable, saved: boolean = true) {
    let key = this.getKey(queueable);
    this.saved[key] = saved;
    delete queueable['saved'];  // unused after entry is in saved map

    if (queueable['type'] == 'album') {
      // if an album is saved/removed, all of its songs are saved or removed
      queueable['songs'].forEach(song => {
        let sKey = this.getKey(song);
        this.saved[sKey] = saved;
        delete song['saved'];
      });
    } else if (queueable['type'] == 'song' && !saved) {
      // if one or more songs from an album is removed, it is no longer saved
      let aKey = this.getKey(queueable['album']);
      this.saved[aKey] = false;
    }
  }

  private getEndpoint(queueable: Queueable): string {
    if (queueable['type'] == 'song') {
      return '/api/customer/library/songs/' + queueable['id'];
    } else {
      return '/api/customer/library/albums/' + queueable['id'];
    }
  }

  private getKey(queueable: Queueable) {
    return queueable['type'] + queueable['id'];
  }

  private handleError(action: string, err: any) {
    // todo: display error message
    console.log(action, 'failed. error was:', err);
  }

  getSongs(page: number): Observable<PlaylistSong[]> {
    let params = new HttpParams();
    params = params.set("page", page.toString());
    return this.client.get<PlaylistSong[]>('/api/customer/library/songs', { params: params });
  }

  getAlbums(page: number): Observable<Album[]> {
    let params = new HttpParams();
    params = params.set("page", page.toString());
    return this.client.get<Album[]>('/api/customer/library/albums', { params: params });
  }

  getArtists(page: number): Observable<Artist[]> {
    let params = new HttpParams();
    params = params.set("page", page.toString());
    return this.client.get<Artist[]>('/api/customer/library/artists', { params: params });
  }
}
