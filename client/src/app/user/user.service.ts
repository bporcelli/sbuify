import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs/Rx";
import { JwtService } from "../auth/jwt.service";
import { User } from "./user";

/**
 * Service to get information about the authenticated user.
 */
@Injectable()
export class UserService {
  private currentUserSubject: BehaviorSubject<User> = new BehaviorSubject(null);
  private isAuthenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject(false);

  constructor(private jwtService: JwtService) {
    this.populate();
  }

  /** Populate the current user based on the local JWT. */
  private populate(): void {
    if (this.jwtService.hasToken()) {
      let token = this.jwtService.getDecodedToken();

      this.currentUserSubject.next(new User(token['sub'], '', token['scopes'][0]));
      this.isAuthenticatedSubject.next(true);
    } else {
      this.currentUserSubject.next(null);
      this.isAuthenticatedSubject.next(false);
    }
  }

  /** Set the user's authentication token. */
  setAuth(token: string, remember: boolean) {
    this.jwtService.setToken(token, remember);
    this.populate();
  }

  /** Clear the user's authentication token. */
  clearAuth(): void {
    this.jwtService.clearToken();
    this.populate();
  }

  get isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  get currentUser(): Observable<User> {
    return this.currentUserSubject.asObservable().distinctUntilChanged();
  }
}
