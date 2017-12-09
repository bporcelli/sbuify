import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Playlist } from "./playlist";
import { SongList } from "../songs/song-list";
import { Song } from "../songs/song";
import { PlayerService } from "app/playback/player.service";
import { PlaylistService } from "./playlist.service";
import { PlaylistSong } from "./playlist-song";
import { Config } from "../config";
import { User } from "../user/user";
import { UserService } from "../user/user.service";
import { PlaylistModalComponent } from "./playlist-modal.component";

@Component({
  templateUrl: './playlist-detail.component.html',
})
export class PlaylistDetailComponent implements OnInit {

  /** Current filter value */
  private filter: string = '';

  /** Full list of songs. */
  private unfiltered: Song[] = [];

  /** Filtered songs. */
  private songList: SongList = new SongList([]);

  /** Current page in songs list. */
  private page: number = 0;

  /** Is there a pending request for the next page of songs? */
  public pending: boolean = false;

  /** Are there more results available? */
  private more: boolean = true;

  /** Playlist */
  playlist: Playlist;

  /** Current user */
  private user: User = null;

  constructor(
    private route: ActivatedRoute,
    private playlistService: PlaylistService,
    private playService: PlayerService,
    private userService: UserService,
    private modalService: NgbModal,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.data
      .subscribe((data: { playlist: Playlist }) => {
        this.playlist = data.playlist;

        this.reset();
        this.nextPage();
      });

    this.userService.currentUser
      .subscribe((user) => this.user = user);
  }

  private reset() {
    this.page = 0;
    this.songList.songs = [];
    this.unfiltered = [];
    this.more = true;
  }

  /** Update visible rows when filter changes */
  onFilterChange(value: string = null): void {
    if (value == null) {  // use existing filter value
      value = this.filter;
    }

    value = value.toLowerCase();

    const temp = this.unfiltered.filter((song: Song) => {
      return song.name.toLowerCase().indexOf(value) !== -1 || !value;
    });

    this.songList.songs = temp;
    this.filter = value;
  }

  /** Toggle playback of the song list */
  togglePlayback(): void {
    if (!this.isPlaylist()) {
      this.playService.play(this.songList);
    } else {
      this.playService.toggle();
    }
  }

  /** Check: is the song list playing? */
  isPlaying(): boolean {
    return this.isPlaylist() && this.playService.playing.value;
  }

  private isPlaylist(): boolean {
    return this.playService.isPlaying(this.songList);
  }

  /** Check: does the current user own the playlist? */
  isUserOwned(): boolean {
    return this.user != null && this.user.email == this.playlist.owner.email;
  }

  /** Open the 'Edit Playlist' modal */
  editPlaylist(): void {
    let modal = this.modalService.open(PlaylistModalComponent);
    let comp = modal.componentInstance;

    comp.id = this.playlist.id;
    comp.name = this.playlist.name;
    comp.description = this.playlist.description;
    comp.hidden = this.playlist.hidden;

    if (this.playlist.image) {
      comp.imageURL = '/static/i/' + this.playlist.image['id'] + '/full';
    }
  }

  /** Delete the playlist */
  deletePlaylist(): void {
    if (confirm('Are you sure you want to delete this playlist? This action is irreversible.')) {
      this.playlistService.delete(this.playlist)
        .subscribe(() => {
          this.router.navigate(['/browse/overview']);
        });
    }
  }

  /** Follow or unfollow the playlist */
  toggleFollowing(): void {
    this.playlistService.followOrUnfollow(this.playlist);
  }

  /** Handle song removal */
  onSongRemoved(song: Song): void {
    this.unfiltered = this.unfiltered.filter(item => item.id != song.id);
    this.onFilterChange();
  }

  private nextPage() {
    if (this.pending) {  // already waiting for next page
      return;
    } else if (!this.more) {  // no more results available
      return;
    }

    this.pending = true;

    this.playlistService.getSongs(this.playlist.id, this.page)
      .subscribe(
        (songs: PlaylistSong[]) => this.handleNewSongs(songs),
        (err: any) => this.handleError(err)
      );
  }

  private handleNewSongs(plSongs: PlaylistSong[]) {
    this.pending = false;

    let songs = plSongs.map(plSong => plSong.song);

    this.unfiltered.push(...songs);
    this.onFilterChange();
    this.page += 1;

    if (songs.length < Config.ITEMS_PER_PAGE) {
      this.more = false;
    }
  }

  private handleError(err: any) {
    // todo: display error
    console.log('error getting next page of songs:', err);
  }

  get songs(): SongList {
    return this.songList;
  }
}
