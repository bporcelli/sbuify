import { Injectable } from '@angular/core';
import { Router } from "@angular/router";

import { JwtHelper } from 'angular2-jwt';

import { Config } from "../config";
import { tokenGetter } from "./helpers";

@Injectable()
export class AuthService {
  // URL to redirect to after login
  redirectURL: string = Config.DEFAULT_REDIRECT;

  constructor(private router: Router) {}

  /**
   * Determine whether we have a valid JWT for the current user.
   * @returns {boolean}
   */
  isAuthed() {
    return this.tokenNotExpired();
  }

  /**
   * Authenticate a user by storing their JWT. Afterward, redirect to the saved redirectURL.
   */
  login(token: string, remember: boolean) {
    localStorage.setItem('remember', remember ? 'true' : 'false');

    if (remember) {
      localStorage.setItem('token', token);
    } else {
      sessionStorage.setItem('token', token);  // expires after page is closed
    }

    this.router.navigate([this.redirectURL]);
  }

  /**
   * Log out the current user, then redirect to the login page.
   */
  logout() {
    var remember = localStorage.getItem('remember');

    // clear remember flag
    localStorage.removeItem('remember');

    // clear token
    if (remember == 'true') {
      localStorage.removeItem('token');
    } else {
      sessionStorage.removeItem('token');
    }

    // go to login
    this.router.navigate(['login']);
  }

  /**
   * Return a boolean indicating whether the stored JWT is valid (not expired).
   */
  private tokenNotExpired() {
    let getter = tokenGetter();
    let token = getter();
    let jwtHelper = new JwtHelper();

    return token != null && !jwtHelper.isTokenExpired(token);
  }
}
