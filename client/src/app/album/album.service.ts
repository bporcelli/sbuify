import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { HttpParams } from '@angular/common/http';
import { APIClient } from "../shared/api-client.service";
import { Album } from "./album";

@Injectable()
export class AlbumService {

  /** Locally cached albums */
  private cache: object = {};

  constructor(private client: APIClient) {}

  getRecent(page: number): Observable<Array<Album>> {
    let params: HttpParams = new HttpParams();
    params = params.set("page", page.toString());
    return this.client.get<Array<Album>>("/api/albums/new-releases", { params: params })
      .map((result: Album[]) => this.saveAll(result));
  }

  getAlbum(id: any): Observable<Album> {
    if (id in this.cache) {
      return Observable.of(this.cache[id]);
    }
    return this.client.get<Album>('/api/albums/' + id)
      .map((album: Album) => this.save(album));
  }

  /** Save an album to the local cache */
  private save(album: Album): Album {
    this.cache[album.id] = album;
    return album;
  }

  /** Save a list of albums to the local cache */
  private saveAll(albums: Album[]): Album[] {
    for (let album of albums) {
      this.save(album);
    }
    return albums;
  }
}
