import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs/Rx";
import { JwtService } from "../auth/jwt.service";
import { APIClient } from "../shared/api-client.service";
import { Customer } from "./customer";

/**
 * Service to get information about the authenticated user.
 */
@Injectable()
export class UserService {
  private currentUserSubject: BehaviorSubject<Customer> = new BehaviorSubject(null);
  private isAuthenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject(false);

  constructor(
    private jwtService: JwtService,
    private apiClient: APIClient
  ) {
    // must subscribe to trigger GET request
    this.populate().subscribe();
  }

  /** Populate the current user based on the local JWT. */
  private populate(): Observable<Customer> {
    if (!this.jwtService.hasToken()) {  // no valid token available
      return Observable.of(null);
    }

    this.isAuthenticatedSubject.next(true);

    return this.apiClient.get<Customer>('/api/customer/')
      .catch(this.handleError)
      .map((customer: Customer) => {
        this.currentUserSubject.next(customer);
        return customer;
      });
  }

  /** Set the user's authentication token. */
  authenticate(token: string, remember: boolean): Observable<Customer> {
    this.jwtService.setToken(token, remember);
    return this.populate();
  }

  /** Clear the user's authentication token. */
  clearAuth(): void {
    this.jwtService.clearToken();

    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
  }

  /** Update the current user's name, email, and/or birthday */
  updateUser(user: Customer): Observable<void> {
    return this.apiClient.patch('/api/customer/', user)
      .catch(this.handleError)
      .map(() => {
        this.currentUserSubject.next(user);
      });
  }

  /** Create a premium subscription for the user */
  createSubscription(card: object): Observable<void> {
    return this.apiClient.post('/api/customer/subscription', card)
      .catch(this.handleError)
      .map(() => {
        let user = this.currentUserSubject.value;
        user.premium = true;
        this.currentUserSubject.next(user);
      });
  }

  /** Cancel a user's subcription */
  cancelSubscription(): Observable<void> {
    return this.apiClient.delete('/api/customer/subscription')
      .catch(this.handleError)
      .map(() => {
        let user = this.currentUserSubject.value;
        user.premium = false;
        this.currentUserSubject.next(user);
      });
  }

  get isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  get currentUser(): Observable<Customer> {
    return this.currentUserSubject.asObservable().distinctUntilChanged();
  }

  private handleError(err: any, caught: Observable<any>): Observable<any> {
    // todo: show error
    let message = err['error'] ? err['error'] : err;
    console.log('error occurred while getting user information:', message);
    return Observable.throw(err);
  }
}
