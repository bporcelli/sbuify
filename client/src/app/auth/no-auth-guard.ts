import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { UserService } from "../user/user.service";

@Injectable()
export class NoAuthGuard implements CanActivate {
  constructor(private userService: UserService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.userService.isAuthenticated.take(1).map((val: boolean) => !val);
  }
}
