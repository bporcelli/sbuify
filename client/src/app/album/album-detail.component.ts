import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Album } from "./album";
import { PlayQueueService } from "../play-queue/play-queue.service";
import { PlayerService } from "../playback/player.service";
import { Song} from "../songs/song";
import { Playable } from "app/playback/playable";
import { Queueable } from "../play-queue/queueable";
import { LibraryService } from "../library/library.service";
import { SongContextMenuComponent } from "../songs/song-context-menu.component";

@Component({
  templateUrl: './album-detail.component.html'
})
export class AlbumDetailComponent implements OnInit {

  @ViewChild(SongContextMenuComponent) menu: SongContextMenuComponent;

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
    let target = songIndex == null ? this.album : this.album.songs[songIndex];

    if (!this.isNowPlaying(target)) {
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
    this.libService.saveOrRemove(queueable).subscribe();
  }

  /** Add an album or song to the play queue */
  addToQueue(queueable: Queueable): void {
    this.queueService.add(queueable);
  }

  /** Determine whether the given album or song is playing */
  isPlaying(playable: Playable): boolean {
    return this.isNowPlaying(playable) && this.playService.playing.value;
  }

  private isNowPlaying(playable: Playable): boolean {
    return this.playService.isPlaying(playable);
  }

  /** Determine whether the given album or song is saved */
  isSaved(queueable: Queueable): boolean {
    return this.libService.isSaved(queueable);
  }

  /** Open the song context menu */
  onContextMenu(event: MouseEvent, item: any): void {
    this.menu.open(event, item);
  }

  get songs(): Song[] {
    return this.album ? this.album.songs : [];
  }
}
