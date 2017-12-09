import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Album } from "./album";
import { PlayQueueService } from "../play-queue/play-queue.service";
import { PlayerService } from "../playback/player.service";
import {Song} from "../songs/song";
import {Playable} from "app/playback/playable";
import {Queueable} from "../play-queue/queueable";
import {LibraryService} from "../library/library.service";

@Component({
  templateUrl: './album-detail.component.html'
})
export class AlbumDetailComponent implements OnInit {

  /** Album being displayed */
  album: Album;

  constructor(
    private router: Router,
    private queueService: PlayQueueService,
    private playService: PlayerService,
    private libService: LibraryService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.data
      .subscribe((data: { album: Album }) => this.album = data.album);
  }

  /** Toggle playback of the album */
  togglePlayback(songIndex?: number): void {
    if (!this.isCurrentPlaylist(this.album) || songIndex != null) {
      songIndex = songIndex == null ? 0 : songIndex;
      this.playService.play(this.album, songIndex);
    } else {
      this.playService.toggle();
    }
  }

  /** Open the album artist's page */
  openArtistPage(): void {
    this.router.navigate(['/artist', this.album.artist.id]);
  }

  /** Save or remove the album from the user's library */
  toggleSaved(queueable: Queueable): void {
    this.libService.saveOrRemove(queueable);
  }

  /** Add an album or song to the play queue */
  addToQueue(queueable: Queueable): void {
    this.queueService.add(queueable);
  }

  /** Remove a song from the play queue */
  removeFromQueue(song: Song): void {
    this.queueService.remove(song);
  }

  /** Determine whether the given album or song is playing */
  isPlaying(playable: Playable): boolean {
    return this.isCurrentPlaylist(playable) && this.playService.playing.value;
  }

  private isCurrentPlaylist(playable: Playable): boolean {
    return this.playService.isPlaying(playable);
  }

  /** Determine whether the given album or song is saved */
  isSaved(queueable: Queueable): boolean {
    return this.libService.isSaved(queueable);
  }

  /** Check whether a song is enqueued */
  isEnqueued(item: any): boolean {
    return item != null && item['queued'];
  }

  get songs(): Song[] {
    return this.album ? this.album.songs : [];
  }
}
