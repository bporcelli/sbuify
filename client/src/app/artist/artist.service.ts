import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Artist } from "./artist";
import { Observable } from 'rxjs/Rx';
import { APIClient } from "../shared/api-client.service";
import { SongList } from "../songs/song-list";
import { Album } from "../album/album";
import { FollowingService } from "../user/following.service";
import { Biography } from "./biography";

@Injectable()
export class ArtistService {

  /** Local artist cache */
  private artists: object = {};

  /** The artist being viewed, if any */
  public current: Artist = null;

  constructor(
    private apiClient: APIClient,
    private followService: FollowingService
  ) {}

  /** Get information about an artist */
  getArtist(id: number): Observable<Artist> {
    if (id in this.artists) {
      return Observable.of(this.artists[id]);
    }
    return this.apiClient.get<Artist>('/api/artists/' + id)
      .map((artist: Artist) => {
        // ensure that songs have a type set
        for (let song of artist.popularSongs) {
          song.type = 'song';
        }
        // make the artist's popular songs playable
        artist.popularSongs = new SongList(artist.popularSongs, 'artist', artist.id);
        this.artists[id] = artist;
        return artist;
      });
  }

  /** Get artists related to an artist */
  getRelated(id: number, count: number = 5): Observable<Artist[]> {
    let related = [];
    if (id in this.artists) {
      related = this.artists[id].related || [];
    }
    if (related.length >= count) {
      // serve from local store
      return Observable.of(related.slice(0, count));
    }
    let required = count - related.length;
    let params = new HttpParams().set("offset", related.length.toString());
    return this.apiClient.get<Artist[]>('/api/artists/' + id + '/related/' + required, { params: params })
      .map((artists: Artist[]) => {
        related.push(...artists);
        this.artists[id].related = related;
        return related.slice(0, count);
      });
  }

  /** Get artist merchandise */
  getMerchandise(id: number): Observable<object[]> {
    if (id in this.artists && 'merchandise' in this.artists[id]) {
      return Observable.of(this.artists[id]['merchandise']);
    }
    return this.apiClient.get<object[]>('/api/artists/' + id + '/merchandise')
      .map((merch: object[]) => {
        this.artists[id]['merchandise'] = merch;
        return merch;
      });
  }

  /** Get artist albums */
  getAlbums(id: number): Observable<Album[]> {
    if (id in this.artists && 'albums' in this.artists[id]) {
      return Observable.of(this.artists[id]['albums']);
    }
    return this.apiClient.get<Album[]>('/api/artists/' + id + '/albums')
      .map((albums: Album[]) => {
        this.artists[id]['albums'] = albums;
        return albums;
      });
  }

  /** Follow or unfollow an artist */
  toggleFollowing(artist: Artist): Observable<boolean> {
    if (artist['followed']) {
      return this.followService.unfollow('artist', artist.id)
        .map((followed) => {
          artist['followed'] = followed;
          artist['numFollowers']--;
          this.artists[artist.id] = artist;
          return followed;
        });
    } else {
      return this.followService.follow('artist', artist.id)
        .map((followed) => {
          artist['followed'] = followed;
          artist['numFollowers']++;
          this.artists[artist.id] = artist;
          return followed;
        });
    }
  }

  /** Get an artist's biography */
  getBiography(id: number): Observable<Biography> {
    if (id in this.artists && 'biography' in this.artists[id]) {
      return Observable.of(this.artists[id]['biography']);
    }
    return this.apiClient.get<Biography>('/api/artists/' + id + '/bio')
      .map((bio) => {
        this.artists[id]['biography'] = bio;
        return bio;
      });
  }
}
