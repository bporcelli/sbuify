import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';

import { PlaylistService } from './playlist.service';

@Injectable()
export class PlaylistDetailResolver implements Resolve<Object> {
    constructor(private service: PlaylistService, private router: Router) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Object> {
        let id = route.paramMap.get('id');

        return this.service.getPlaylist(id).take(1).map(playlist => {
            if (playlist) {
                return playlist;
            } else { // id not found
                return null;
            }
        });
    }
}
