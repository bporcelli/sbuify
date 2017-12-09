import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { PlaylistService } from "./playlist.service";
import { User } from "../user/user";
import { UserService } from "../user/user.service";
import { PlaylistFolderModalComponent } from "./playlist-folder-modal.component";
import { PlaylistModalComponent } from "./playlist-modal.component";
import { Playlist } from "./playlist";

@Component({
  selector: 'playlist-folder',
  templateUrl: './playlist-folder.component.html'
})
export class PlaylistFolderComponent implements OnInit {
  @Input() folder: object = null;

  /** Current user */
  private user: User = null;

  /** Required to reference 'this' in ngx-contextmenu visible functions */
  public isUserOwnedBound = this.isUserOwned.bind(this);
  public canCreateFolderBound = this.canCreateFolder.bind(this);
  public isNotUserOwnedBound = this.isNotUserOwned.bind(this);

  /** Is the folder collapsed? */
  isCollapsed: boolean = true;

  /** Playlists in the folder. */
  playlists: any[] = [];

  /** Have we initialized the playlists list? */
  initialized: boolean = false;

  constructor(
    private playlistService: PlaylistService,
    private userService: UserService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.playlistService.getFolder(this.folderId)
      .filter((value: object[]) => value != null)
      .subscribe(
        (playlists: object[]) => this.setPlaylists(playlists),
        (err: any) => this.handleError(err)
      );

    this.userService.currentUser
      .subscribe(user => this.user = user);
  }

  /** Expand or collapse the folder tree */
  toggleCollapse(event: MouseEvent): void {
    this.isCollapsed = !this.isCollapsed;
    event.preventDefault();
  }

  /** Open the create playlist modal. */
  openPlaylistModal(item: any): void {
    let modal = this.modalService.open(PlaylistModalComponent);

    if (item && item.folder) {
      modal.componentInstance.folder = item;
    } else {
      modal.componentInstance.folder = this.folder;
    }
  }

  /** Open the create folder modal. */
  openFolderModal(): void {
    this.modalService.open(PlaylistFolderModalComponent);
  }

  /** Check whether a playlist or folder is owned by the current user */
  isUserOwned(item: any): boolean {
    return this.user != null && item != null && this.user.email == item.owner.email;
  }

  /** Check whether a playlist or folder is NOT owned by the current user */
  isNotUserOwned(item: any): boolean {
    return !this.isUserOwned(item);
  }

  /** Unfollow a playlist */
  unfollow(item: Playlist): void {
    this.playlistService.followOrUnfollow(item);
  }

  /** Can we create a folder at this level of the playlist folder hierarchy? */
  canCreateFolder(item: any): boolean {
    return this.folder == null && item != null && !item.folder;
  }

  /** Edit a playlist or folder */
  edit(item: any): void {
    if (item.folder) {
      let modal = this.modalService.open(PlaylistFolderModalComponent);
      let comp = modal.componentInstance;

      comp.id = item.id;
      comp.name = item.name;
    } else {
      let modal = this.modalService.open(PlaylistModalComponent);
      let comp = modal.componentInstance;

      comp.id = item.id;
      comp.name = item.name;
      comp.description = item.description;
      comp.hidden = item.hidden;

      if (item.image) {
        comp.imageURL = '/static/i/' + item.image.id + '/full';  // todo: get catalog size instead
      }
    }
  }

  /** Delete a playlist or folder */
  delete(item: any): void {
    let message = 'Are you sure you want to delete this playlist?';

    if (item.folder) {
      message = 'Are you sure you want to delete this folder and all playlists it contains?';
    }

    if (confirm(message)) {
      this.playlistService.delete(item)
        .subscribe(
          () => {},
          (err: any) => this.handleError(err)
        );
    }
  }

  private setPlaylists(playlists: object[]): void {
    this.initialized = true;
    this.playlists = playlists;
  }

  private handleError(err: any): void {
    // todo: show error
    console.log('error occurred while getting folder:', err);
  }

  get folderId(): number {
    return this.folder ? this.folder['id'] : 0;
  }

  get collapseId(): string {
    return 'collapse' + this.folderId;
  }

  get listClass(): string {
    return this.folder ? 'submenu' : 'nav sidebar-nav flex-column';
  }

  get isRoot(): boolean {
    return this.folder == null;
  }
}
