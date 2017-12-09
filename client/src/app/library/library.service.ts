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

  /** Dictionary for keeping track of saved songs (initial values taken from server response) */
  private saved: object = {};

  constructor(private client: APIClient) {}

  /** Save or remove a song from the authenticated customer's library. */
  saveOrRemove(queueable: Queueable): Observable<boolean> {
    if (this.isSaved(queueable)) {
      return this.remove(queueable);
    } else {
      return this.save(queueable);
    }
  }

  /** Determine whether a song or album is saved. */
  isSaved(queueable: Queueable): boolean {
    if (queueable['type'] == 'song') {
      let key = this.getKey(queueable);

      if (!(key in this.saved)) {
        this.saved[key] = queueable['saved'];
      }
      return this.saved[key];
    } else {
      // albums are saved if all of their songs are saved
      let saved = true;

      for (let song of queueable['songs']) {
        if (!this.isSaved(song)) {
          saved = false;
          break;
        }
      }
      return saved;
    }
  }

  private remove(queueable: Queueable): Observable<boolean> {
    let endpoint = this.getEndpoint(queueable);

    return this.client.delete(endpoint)
      .catch(this.handleError)
      .map(() => {
        this.setSaved(queueable, false);
        return false;
      });
  }

  private save(queueable: Queueable): Observable<boolean> {
    let endpoint = this.getEndpoint(queueable);

    return this.client.post(endpoint, null)
      .catch(this.handleError)
      .map(() => {
        this.setSaved(queueable);
        return true;
      });
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

  private handleError(err: any, caught: Observable<any>): Observable<any> {
    // todo: display error message
    console.log('failed to save or remove queueable. error was:', err);
    return Observable.throw(err);
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
