import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Playlist } from "./playlist";
import { BehaviorSubject, Observable } from "rxjs/Rx";
import { PlaylistService } from "./playlist.service";
import { CreatePlaylistComponent } from "./create-playlist.component";

@Component({
  selector: 'playlist-list',
  templateUrl: './playlist-list.component.html'
})
export class PlaylistListComponent implements OnInit {
  private _playlists: BehaviorSubject<Playlist[]> = new BehaviorSubject([]);

  constructor(
    private playlistService: PlaylistService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.playlistService.getAll()
      .subscribe(
        (playlists: Playlist[]) => this._playlists.next(playlists),
        (error: any) => this.handleError(error)
      );
  }

  /** Open the create playlist modal. */
  openModal(): void {
    this.modalService.open(CreatePlaylistComponent);
  }

  private handleError(err: any) {
    // todo: show error message
    console.log('error occurred while fetching playlists:', err);
  }

  get playlists(): Observable<Playlist[]> {
    return this._playlists.asObservable().distinctUntilChanged();
  }
}
