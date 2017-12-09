import { Component, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { ContextMenuComponent, ContextMenuService } from 'ngx-contextmenu';
import { Song } from "./song";
import { PlayQueueService } from "../play-queue/play-queue.service";
import { LibraryService } from "../library/library.service";
import { PlaylistService } from "../playlist/playlist.service";
import { Playlist } from "../playlist/playlist";

@Component({
  selector: 'song-context-menu',
  templateUrl: './song-context-menu.component.html'
})
export class SongContextMenuComponent {
  @ViewChild(ContextMenuComponent) contextMenu: ContextMenuComponent;

  /** Is the menu being display on an album page? */
  @Input() album: boolean = false;

  /** Playlist if the menu is being displayed on a playlist page */
  @Input() playlist: Playlist = null;

  /** Event parent components can subscribe to be notified when a song is removed */
  @Output() onRemoved: EventEmitter<any> = new EventEmitter();

  constructor(
    private contextMenuService: ContextMenuService,
    private pqs: PlayQueueService,
    private router: Router,
    private libService: LibraryService,
    private playlistService: PlaylistService
  ) {}

  open(event: MouseEvent, song: Song): void {
    this.contextMenuService.show.next({
      contextMenu: this.contextMenu,
      event: event,
      item: song
    });
    event.preventDefault();
    event.stopPropagation();
  }

  /** Enqueue a song. */
  addToQueue(song: Song): void {
    this.pqs.add(song);
  }

  /** Remove an enqueued song. */
  removeFromQueue(song: Song): void {
    this.pqs.remove(song);
  }

  /** Check whether a song is enqueued */
  isEnqueued(item: any): boolean {
    return item != null && item['queued'];
  }

  /** Save a song to or remove a song from the customer's library. */
  saveOrRemove(song: Song): void {
    this.libService.saveOrRemove(song)
      .subscribe((saved: boolean) => {
        if (!saved) {
          this.onRemoved.emit(song);
        }
      });
  }

  /** Check whether a song is saved */
  isSaved(item: any): boolean {
    return item != null && this.libService.isSaved(item);
  }

  /** Open the album page for a song */
  openAlbumPage(song: Song): void {
    this.router.navigate(['/album', song.album.id]);
  }

  /** Open the artist page for a song */
  openArtistPage(song: Song): void {
    this.router.navigate(['/artist', song.album.artist.id]);
  }

  /** Add a song to a playlist */
  addToPlaylist(song: Song, playlist: Playlist): void {
    this.playlistService.add(song, playlist);
  }

  /** Remove a song from the playlist being viewed */
  removeFromPlaylist(song: Song): void {
    this.playlistService.remove(song, this.playlist)
      .subscribe(() => this.onRemoved.emit(song));
  }

  get userOwnedPlaylists(): object[] {
    return this.playlistService.getUserOwned();
  }

  get isPlaylist(): boolean {
    return this.playlist != null;
  }

  get isUserOwnedPlaylist(): boolean {
    return this.isPlaylist && this.userOwnedPlaylists.indexOf(this.playlist) !== -1;
  }
}
