import { Component, Input } from '@angular/core';
import { PlaylistService } from "./playlist.service";

@Component({
  selector: 'playlist-folder',
  template: `
    <a class="nav-link toggle-collapse" [href]="'#' + collapseId" (click)="toggleCollapse($event)" [ngClass]="isCollapsed ? 'collapsed' : 'expanded'">
      {{ folder.name }}
    </a>
    <ul [id]="collapseId" [ngbCollapse]="isCollapsed" class="submenu">
      <ng-container *ngIf="playlists.length > 0 || pending; else noPlaylists">
        <li *ngFor="let playlist of playlists">
          <a class="nav-link" [routerLink]="['playlist', playlist.id]" routerLinkActive="active">{{ playlist.name }}</a>
        </li>
      </ng-container>
      <ng-template #noPlaylists>
         <li class="nav-item nav-link">Folder is empty.</li>
      </ng-template>
      <li class="nav-link" [hidden]="!pending">
        Loading...
      </li>
    </ul>
  `
})
export class PlaylistFolderComponent {
  @Input() folder: object;

  /** Is the folder collapsed? */
  isCollapsed: boolean = true;

  /** Is a request for the folder's playlists pending? */
  pending: boolean = false;

  /** Playlists in the folder. */
  playlists: any[] = [];

  /** Have we initialized the playlists list? */
  private initialized: boolean = false;

  constructor(private playlistService: PlaylistService) {}

  toggleCollapse(event: Event): void {
    event.preventDefault();

    this.isCollapsed = !this.isCollapsed;

    if (!this.initialized) {
      this.pending = true;

      this.playlistService.getFolder(this.folder['id'])
        .subscribe(
          (playlists: object[]) => this.setPlaylists(playlists),
          (err: any) => this.handleError(err)
        );
    }
  }

  get collapseId(): string {
    return 'collapse' + this.folder['id'];
  }

  private setPlaylists(playlists: object[]): void {
    this.initialized = true;
    this.pending = false;
    this.playlists.push(...playlists);
  }

  private handleError(err: any): void {
    // todo: show error
    console.log('error occurred while getting folder:', err);
  }
}
