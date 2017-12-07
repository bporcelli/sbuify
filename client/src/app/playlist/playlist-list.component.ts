import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Playlist } from "./playlist";
import { BehaviorSubject, Observable } from "rxjs/Rx";
import { PlaylistService } from "./playlist.service";
import { CreatePlaylistComponent } from "./create-playlist.component";
import { UserService } from "../user/user.service";
import { User } from "../user/user";
import { CreatePlaylistFolderComponent } from "./create-playlist-folder.component";

@Component({
  selector: 'playlist-list',
  templateUrl: './playlist-list.component.html'
})
export class PlaylistListComponent implements OnInit {

  /** Current user */
  private user: User = null;

  private _playlists: BehaviorSubject<Playlist[]> = new BehaviorSubject([]);

  /** Bound version of isUsedOwned function -- required to access this in the function */
  public isUserOwnedBound = this.isUserOwned.bind(this);

  /** Bound version of the isFollowed function */
  public isFollowedBound = this.isFollowed.bind(this);

  constructor(
    private playlistService: PlaylistService,
    private modalService: NgbModal,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.playlistService.getAll()
      .subscribe(
        (playlists: Playlist[]) => this._playlists.next(playlists),
        (error: any) => this.handleError(error)
      );

    this.userService.currentUser
      .subscribe(user => this.user = user);
  }

  /** Open the create playlist modal. */
  openPlaylistModal(): void {
    this.modalService.open(CreatePlaylistComponent);
  }

  /** Open the create folder modal. */
  openFolderModal(): void {
    this.modalService.open(CreatePlaylistFolderComponent);
  }

  /** Check whether a playlist or folder is owned by the current user */
  isUserOwned(item: any): boolean {
    return this.user != null && item != null && this.user.email == item.owner.email;
  }

  /** Check whether a playlist is followed by the current user */
  isFollowed(item: any): boolean {
    // todo
    return !item.folder && false;
  }

  /** Unfollow a playlist */
  unfollow(item: Playlist): void {
    console.log('would unfollow', item);
  }

  /** Check whether a given item is NOT a folder */
  isNotFolder(item: any): boolean {
    return item != null && !item.folder;
  }

  /** Edit a playlist or folder */
  edit(item: any): void {
    if (item.folder) {
      console.log('would edit folder', item);
    } else {
      console.log('would edit playlist', item);
    }
  }

  /** Delete a playlist or folder */
  delete(item: any): void {
    this.playlistService.delete(item)
      .subscribe(
        () => {},
        (err: any) => this.handleError(err)
      );
  }

  private handleError(err: any) {
    // todo: show error message
    console.log('error occurred while fetching playlists:', err);
  }

  get playlists(): Observable<Playlist[]> {
    return this._playlists.asObservable().distinctUntilChanged();
  }
}
