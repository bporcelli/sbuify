import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';

import { Genre } from "./genre";
import { GenreService } from "./genre.service";

@Injectable()
export class GenreResolver implements Resolve<Array<Genre>> {
  constructor(private service: GenreService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Array<Genre>> {
    return this.service.getAll().take(1).map((value: Array<Genre>) => {
      if (value.length == 0) {
        // todo: show error message
        return null;
      } else {
        return value;
      }
    });
  }
}
