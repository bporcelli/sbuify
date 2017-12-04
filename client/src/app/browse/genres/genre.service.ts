import { Injectable } from "@angular/core";
import { Genre } from "./genre";
import { Observable } from "rxjs/Rx";
import { APIClient } from "../../api/api-client.service";
import { Album } from "../../album/album";

@Injectable()
export class GenreService {

  // todo: cache server responses
  constructor(private client: APIClient) {}

  getAll(): Observable<Array<Genre>> {
    return this.client.get<Array<Genre>>("/api/genres");
  }

  get(id: string): Observable<Genre> {
    return this.client.get<Genre>("/api/genres/" + id);
  }

  getPopular(id: number): Observable<Array<Album>> {
    return this.client.get<Array<Album>>("/api/genres/" + id + "/popular");
  }

  getRecent(id: number): Observable<Array<Album>> {
    return this.client.get<Array<Album>>("/api/genres/" + id + "/recent");
  }
}
