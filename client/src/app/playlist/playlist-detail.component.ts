import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Playlist } from "./playlist";

@Component({
    templateUrl: './playlist-detail.component.html',
})
export class PlaylistDetailComponent implements OnInit {
    playlist: Playlist;

    constructor(private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data
            .subscribe((data: { playlist: Playlist }) => {
                this.playlist = data.playlist;
            });
    }
}
