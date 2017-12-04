import { map, take } from 'rxjs/operators';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot, Router } from '@angular/router';

import { Genre } from "./genre";
import { GenreService } from "./genre.service";

@Injectable()
export class GenreDetailResolver implements Resolve<Genre> {
  constructor(private service: GenreService,
              private router: Router) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Genre> {
    let id: string = route.paramMap.get('id');

    return this.service.get(id).take(1).map((genre: Genre) => {
      if (genre) {
        return genre;
      } else {
        // invalid genre id -- return to genre list view
        this.router.navigate(['/browse/genres']);
        return null;
      }
    });
  }
}
