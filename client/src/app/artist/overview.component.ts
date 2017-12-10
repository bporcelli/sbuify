import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Artist } from "./artist";
import { Song } from "../songs/song";
import { PlayerService } from "../playback/player.service";
import { LibraryService } from "../library/library.service";
import { Observable, BehaviorSubject } from "rxjs/Rx";
import { ProductModalComponent } from "./product-modal.component";
import { Album } from "../album/album";
import { ArtistService } from "./artist.service";

@Component({
  templateUrl: './overview.component.html',
})
export class OverviewComponent implements OnInit {

  /** Artist being viewed */
  private artist: Artist = null;

  /** Related artists */
  private relatedSubject: BehaviorSubject<Artist[]> = new BehaviorSubject(null);

  /** Merchandise */
  private merchSubject: BehaviorSubject<object[]> = new BehaviorSubject(null);

  /** Unfiltered albums and singles */
  private rawAlbums: Album[] = [];

  /** Type of albums to show ('ALL', 'SINGLE', 'ALBUM') */
  private albumType: string = 'ALL';

  /** Albums and singles after filtering */
  albums: Album[] = [];

  /** True when the artist's albums are loading, otherwise false */
  albumsLoading: boolean = true;

  /** True when we are displaying 10 popular songs, otherwise false */
  showMore: boolean = false;

  constructor(
    private playService: PlayerService,
    private libService: LibraryService,
    private modalService: NgbModal,
    private artistService: ArtistService
  ) {}

  ngOnInit(): void {
    this.artist = this.artistService.current;

    this.artistService.getRelated(this.artist.id)
      .take(1)
      .subscribe((artists: Artist[]) => this.relatedSubject.next(artists));

    this.artistService.getMerchandise(this.artist.id)
      .take(1)
      .filter(merch => merch.length > 0)
      .subscribe((merch: object[]) => this.merchSubject.next(merch));

    this.artistService.getAlbums(this.artist.id)
      .take(1)
      .subscribe((albums: Album[]) => {
        this.rawAlbums = albums;
        this.albumsLoading = false;
        this.onFilterChange(null);
      });
  }

  /** Determine whether the given song is the current song */
  isNowPlaying(song: Song): boolean {
    return this.playService.isPlaying(song);
  }

  /** Determine whether the given song is actively playing */
  isPlaying(song: Song): boolean {
    return this.isNowPlaying(song) && this.playService.playing.value;
  }

  /** Toggle playback of the ith popular song */
  togglePlayback(index: number): void {
    if (!this.isNowPlaying(this.artist.popularSongs)) {
      this.playService.play(this.artist.popularSongs, index);
    } else {
      this.playService.toggle();
    }
  }

  /** Save a song to/remove a song from the user's library */
  saveOrRemoveSong(song: Song): void {
    this.libService.saveOrRemove(song).take(1)
      .subscribe((saved: boolean) => song['saved'] = saved);
  }

  /** Open the merch modal for a particular product */
  openMerchModal(product: any): void {
    let modal = this.modalService.open(ProductModalComponent);
    modal.componentInstance.product = product;
  }

  /** Change the album filter */
  onFilterChange(value: string): void {
    if (value == null) {  // use previous value
      value = this.albumType;
    }

    const temp = this.rawAlbums.filter(album => album['albumType'] == value || value == 'ALL');

    this.albums = temp;
    this.albumType = value;
  }

  get popularSongs(): Song[] {
    return this.artist.popularSongs.songs;
  }

  get relatedArtists(): Observable<Artist[]> {
    return this.relatedSubject.asObservable().distinctUntilChanged();
  }

  get products(): Observable<object[]> {
    return this.merchSubject.asObservable().distinctUntilChanged();
  }
}
