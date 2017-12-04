import 'rxjs/operators/map';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';

const PLAYLISTS = [
    {
        "id": 1,
        "name": "Gold School",
        "author": "bporcelli",
        "image": "assets/img/playlist-placeholder.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        "duration": 1320,
        "songs": [
            {
                "name": "Get Money - Performed by Junion M.A.F.I.A",
                "artist": "The Notorious B.I.G",
                "album": "Greatest Hits",
                "added": "2017-10-08",
                "length": "4:34"
            },
            {
                "name": "All About U",
                "artist": "2Pac",
                "album": "All Eyez On Me",
                "added": "2017-10-08",
                "length": "4:37"
            },
            {
                "name": "Put Your Hands Where My Eyes Could See",
                "artist": "Busta Rhymes",
                "album": "When Disaster Strikes",
                "added": "2017-10-08",
                "length": "3:14"
            },
            {
                "name": "Lost Ones",
                "artist": "Ms. Lauryn Hill",
                "album": "The Miseducation of Lauryn Hill",
                "added": "2017-10-08",
                "length": "5:34"
            },
            {
                "name": "Music Makes Me High",
                "artist": "Lost Boyz",
                "album": "Legal Drug Money",
                "added": "2017-10-08",
                "length": "4:31"
            }
        ]
    },
    {
        "id": 2,
        "name": "Starred",
        "author": "bporcelli",
        "image": "assets/img/playlist-placeholder.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        "duration": 0,
        "songs": []
    }
];

@Injectable()
export class PlaylistService {
    getPlaylists() {
        return Observable.of(PLAYLISTS);
    }

    getPlaylist(id: number | string) {
        return this.getPlaylists()
            // (+) converts id to string
            .map(playlists => playlists.find(playlist => playlist.id == +id));
    }
}
