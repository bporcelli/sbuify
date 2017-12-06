import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Artist } from "./artist";
import { SongList } from "../songs/song-list";
import { PlayerService } from "../player/player.service";

@Component({
  selector: '[artist-grid]',
  templateUrl: './artist-grid.component.html'
})
export class ArtistGridComponent {

  @Input() pending: boolean = true;
  private _artists: Artist[] = [];

  constructor(
    private playService: PlayerService,
    private router: Router
  ) {}

  /** Check whether an artist is playing. */
  isPlaying(artist: Artist): boolean {
    return this.isCurrentPlaylist(artist.popularSongs) && this.playService.playing.value;
  }

  /** Toggle artist playback. */
  togglePlayback(event: Event, artist: Artist): void {
    event.stopPropagation();
    event.preventDefault();

    if (!this.isCurrentPlaylist(artist.popularSongs)) {
      this.playService.play(artist.popularSongs);
    } else {
      this.playService.toggle();
    }
  }

  private isCurrentPlaylist(playlist: SongList): boolean {
    return this.playService.isPlaying(playlist);
  }

  /** Check whether the current user is following an artist. */
  isFollowing(artist: Artist): boolean {
    // todo
    return false;
  }

  /** Follow or unfollow an artist. */
  toggleFollowing(artist: Artist): void {
    // todo
    console.log('would follow or unfollow', artist);
  }

  /** Open an artist page. */
  openArtistPage(artist: Artist): void {
    this.router.navigate(['/artist', artist.id]);
  }

  /** Make artists playable by wrapping their popular songs in a SongList */
  @Input()
  set artists(artists: Artist[]) {
    artists.forEach(artist => {
      artist.popularSongs = new SongList(artist.popularSongs, 'artist-' + artist.id);
    });
    this._artists = artists;
  }

  get artists(): Artist[] {
    return this._artists;
  }
}
