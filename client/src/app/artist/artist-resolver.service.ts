import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Artist } from "./artist";
import { ArtistService } from "./artist.service";

@Injectable()
export class ArtistDetailResolver implements Resolve<Artist> {
  constructor(
    private service: ArtistService,
    private router: Router
  ) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Artist> {
    let id = parseInt(route.paramMap.get('id'));

    return this.service.getArtist(id).take(1).map(artist => {
      if (artist) {
        return artist;
      } else { // id not found
        // todo: show error
        this.router.navigate(['/browse/overview']);
        return null;
      }
    });
  }
}
