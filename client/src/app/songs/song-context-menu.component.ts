import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ContextMenuComponent, ContextMenuService } from 'ngx-contextmenu';
import { Song } from "./song";
import { PlayQueueService } from "../play-queue/play-queue.service";
import { LibraryService } from "../library/library.service";

@Component({
  selector: 'song-context-menu',
  templateUrl: './song-context-menu.component.html'
})
export class SongContextMenuComponent {
  @ViewChild(ContextMenuComponent) contextMenu: ContextMenuComponent;

  constructor(
    private contextMenuService: ContextMenuService,
    private pqs: PlayQueueService,
    private router: Router,
    private libService: LibraryService
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
    this.libService.saveOrRemove(song);
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
}
