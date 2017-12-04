import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from "rxjs/Rx";
import { UserService } from "../user/user.service";

@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private userService: UserService,
              private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    let url: string = state.url;

    return this.checkLogin(url);
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.canActivate(route, state);
  }

  checkLogin(url: string): Observable<boolean> {
    return this.userService.isAuthenticated.map((authenticated) => {
      if (!authenticated) {
        this.router.navigate(['/login', { redirect: encodeURIComponent(url) }]);
      }
      return authenticated;
    });
  }
}
