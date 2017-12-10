import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Artist } from "./artist";
import { PlayerService } from "../playback/player.service";
import { ArtistService } from "./artist.service";

@Component({
  templateUrl: './artist-detail.component.html',
})
export class ArtistDetailComponent implements OnInit, OnDestroy {

  /** The artist being viewed */
  artist: Artist = null;

  constructor(
    private route: ActivatedRoute,
    private playService: PlayerService,
    private artistService: ArtistService
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe((data: { artist: Artist }) => {
      this.artist = data.artist;
      this.artistService.current = data.artist;
    });
  }

  ngOnDestroy(): void {
    this.artistService.current = null;
  }

  /** Determine whether the artist is currently playing */
  isPlaying(): boolean {
    return this.playService.isPlaying(this.artist.popularSongs) && this.playService.playing.value;
  }

  /** Toggle playback of the artist's popular songs */
  togglePlayback(): void {
    if (!this.playService.isPlaying(this.artist.popularSongs)) {
      this.playService.play(this.artist.popularSongs);
    } else {
      this.playService.toggle();
    }
  }

  /** Follow or unfollow the artist */
  toggleFollowing(): void {
    this.artistService.toggleFollowing(this.artist)
      .subscribe();
  }
}
