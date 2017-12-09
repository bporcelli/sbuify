import { Component, Input } from '@angular/core';
import { Router } from "@angular/router";
import { Playlist } from "./playlist";
import { PlayerService } from "../playback/player.service";

@Component({
  selector: '[playlist-grid]',
  templateUrl: './playlist-grid.component.html'
})
export class PlaylistGridComponent {

  /** Playlists to display */
  @Input() playlists: Playlist[] = [];

  /** Is a request for more playlists pending? */
  @Input() pending: boolean = false;

  constructor(
    private playService: PlayerService,
    private router: Router
  ) {}

  /** Toggle playback of a playlist */
  togglePlayback(event: MouseEvent, playlist: Playlist): void {
    event.preventDefault();
    event.stopPropagation();

    if (!this.isQueued(playlist)) {
      this.playService.play(playlist);
    } else {
      this.playService.toggle();
    }
  }

  /** Check: is the given playlist playing? */
  isPlaying(playlist: Playlist): boolean {
    return this.isQueued(playlist) && this.playService.playing.value;
  }

  /** Check: is the given playlist followed by the user? */
  isFollowed(playlist: Playlist) {
    console.log('would check whether', playlist, 'is followed');
  }

  /** Open a playlist */
  openPlaylistPage(playlist: Playlist): void {
    this.router.navigate(['/playlist', playlist.id]);
  }

  /** Follow/unfollow a playlist */
  followOrUnfollow(playlist: Playlist): void {
    console.log('would follow or unfollow', playlist);
  }

  private isQueued(playlist: Playlist): boolean {
    return this.playService.isPlaying(playlist);
  }
}
