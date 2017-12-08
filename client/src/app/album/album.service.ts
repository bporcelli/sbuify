import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { HttpParams } from '@angular/common/http';
import { APIClient } from "../common/api-client.service";
import { Album } from "./album";

@Injectable()
export class AlbumService {

  constructor(private client: APIClient) {}

  getRecent(page: number): Observable<Array<Album>> {
    let params: HttpParams = new HttpParams();
    params = params.set("page", page.toString());
    return this.client.get<Array<Album>>("/api/albums/new-releases", { params: params });
  }
}
