import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Album } from "./album";
import { AlbumService } from "./album.service";

@Injectable()
export class AlbumDetailResolver implements Resolve<Album> {
  constructor(
    private service: AlbumService,
    private router: Router
  ) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Album> {
    let id = route.paramMap.get('id');

    return this.service.getAlbum(id).take(1).map(album => {
      if (album) {
        return album;
      } else { // id not found
        // todo: show error
        this.router.navigate(['/browse/overview']);
        return null;
      }
    });
  }
}
