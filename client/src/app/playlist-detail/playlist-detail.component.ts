import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    templateUrl: './playlist-detail.component.html',
})
export class PlaylistDetailComponent implements OnInit {
    playlist: Object;

    constructor(private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data
            .subscribe((data: { playlist: Object }) => {
                this.playlist = data.playlist;
            });
    }
}