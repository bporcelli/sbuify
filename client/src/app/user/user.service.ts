import { Injectable } from '@angular/core'
import { HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from "rxjs/Rx";
import { JwtService } from "../auth/jwt.service";
import { APIClient } from "../shared/api-client.service";
import { User } from "./user";

/**
 * Service to get information about the authenticated user.
 */
@Injectable()
export class UserService {
  private currentUserSubject: BehaviorSubject<User> = new BehaviorSubject(null);
  private isAuthenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject(false);

  constructor(
    private jwtService: JwtService,
    private apiClient: APIClient
  ) {
    // must subscribe to trigger GET request
    this.populate().subscribe();
  }

  /** Populate the current user based on the local JWT. */
  private populate(): Observable<User> {
    if (!this.jwtService.hasToken()) {  // no valid token available
      return Observable.of(null);
    }

    this.isAuthenticatedSubject.next(true);

    return this.apiClient.get<User>('/api/users/current/')
      .catch(this.handleError)
      .map((user: User) => {
        this.currentUserSubject.next(user);
        return user;
      });
  }

  /** Set the user's authentication token. */
  authenticate(token: string, remember: boolean): Observable<User> {
    this.jwtService.setToken(token, remember);
    return this.populate();
  }

  /** Clear the user's authentication token. */
  clearAuth(): void {
    this.jwtService.clearToken();

    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
  }

  /** Get information about a user given their user ID */
  getUser(id: any): Observable<User> {
    return this.apiClient.get<User>('/api/users/' + id);
  }

  /** Get all users, one page at a time. */
  getAll(page: number): Observable<User[]> {
    let params = new HttpParams();
    params = params.set("page", page.toString());
    return this.apiClient.get<User[]>('/api/users', { params: params });
  }

  /** Get the current user */
  getCurrentUser(): User {
    return this.currentUserSubject.value;
  }

  /** Update the current user */
  setCurrentUser(user: User): void {
    this.currentUserSubject.next(user);
  }

  /** Create a user */
  create(user: User): Observable<User> {
    return this.apiClient.post<User>('/api/users', user);
  }

  /** Update a user */
  update(user: User): Observable<User> {
    let endpoint: string;
    if (user.type == 'admin') {
      endpoint = '/api/admins/' + user.id;
    } else if (user.type == 'customer') {
      endpoint = '/api/customer/' + user.id;
    }
    return this.apiClient.patch<User>(endpoint, user);
  }

  /** Delete a user */
  delete(user: User): Observable<boolean> {
    return this.apiClient.delete('/api/users/' + user['id']).mapTo(true);
  }

  get isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  get currentUser(): Observable<User> {
    return this.currentUserSubject.asObservable().distinctUntilChanged();
  }

  private handleError(err: any, caught: Observable<any>): Observable<any> {
    // todo: show error
    let message = err['error'] ? err['error'] : err;
    console.log('error occurred while getting user information:', message);
    return Observable.throw(err);
  }
}
