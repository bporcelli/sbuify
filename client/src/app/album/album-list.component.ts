import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { Album } from "./album";
import { PlayerService } from "../playback/player.service";
import { PlayQueueService } from "../play-queue/play-queue.service";
import { LibraryService } from "../library/library.service";
import { PlaylistService } from "../playlist/playlist.service";
import { Playlist } from "../playlist/playlist";

@Component({
  selector: '[album-list]',
  templateUrl: './album-list.component.html'
})
export class AlbumListComponent {
  @Input() albums: Array<Album>;
  @Input() pending: boolean;

  /** Event parent components can subscribe to to be notified when albums are removed */
  @Output() onRemoved: EventEmitter<any> = new EventEmitter();

  constructor(
    private ps: PlayerService,
    private pqs: PlayQueueService,
    private libService: LibraryService,
    private router: Router,
    private playlistService: PlaylistService
  ) {}

  toggleAlbumPlayback(event: Event, album: Album) {
    event.stopPropagation();
    event.preventDefault();

    if (!this.isPlaying(album)) {
      this.ps.play(album);
    } else {
      this.ps.toggle();
    }
  }

  getPlayButtonClass(album: Album): string {
    let playing = this.ps.playing.getValue();

    if (playing && this.isPlaying(album)) {
      return 'fa-pause-circle-o';
    }
    return 'fa-play-circle-o';
  }

  isPlaying(album: Album) {
    return this.ps.isPlaying(album);
  }

  /** Save an album to or remove an album from the customer's library. */
  saveOrRemove(album: Album): void {
    this.libService.saveOrRemove(album)
      .subscribe((saved: boolean) => {
        if (!saved) {
          this.onRemoved.emit(album);
        }
      });
  }

  /** Check: is the given album saved in the customer's library? */
  isSaved(album: Album): boolean {
    return album != null && this.libService.isSaved(album);
  }

  /** Add album to play queue */
  enqueue(album: Album): void {
    this.pqs.add(album);
  }

  /** Navigate to album page */
  openAlbumPage(album: Album): void {
    this.router.navigate(['/album', album.id]);
  }

  /** Navigate to album artist page */
  openArtistPage(album: Album): void {
    this.router.navigate(['/artist', album.artist.id]);
  }

  /** Add an album to a playlist */
  addToPlaylist(album: Album, playlist: Playlist): void {
    this.playlistService.add(album, playlist);
  }

  get userOwnedPlaylists(): object[] {
    return this.playlistService.getUserOwned();
  }
}
