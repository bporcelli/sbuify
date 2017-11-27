import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { PlaylistService } from '../playlist/playlist.service';

@Component({
    selector: 'left-sidebar',
    templateUrl: './left-sidebar.component.html',
})
export class LeftSidebarComponent implements OnInit {
    playlists$: Observable<Object>;

    constructor(private playlistService: PlaylistService) {}

    ngOnInit() {
        this.playlists$ = this.playlistService.getPlaylists();
    }
}
