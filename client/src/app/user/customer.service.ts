import { Injectable } from '@angular/core';
import { Customer } from "./customer";
import { Base64Image } from "../shared/base64-image";
import { Image } from "../shared/image";
import { APIClient } from "../shared/api-client.service";
import { FollowingService } from "./following.service";
import { Observable } from 'rxjs/Rx';
import { UserService } from "./user.service";

@Injectable()
export class CustomerService {

  constructor(
    private apiClient: APIClient,
    private followService: FollowingService,
    private userService: UserService
  ) {}

  /** Update the current user's name, email, and/or birthday */
  updateUser(user: Customer): Observable<void> {
    return this.apiClient.patch('/api/customer/', user)
      .catch(this.handleError)
      .map(() => {
        this.userService.setCurrentUser(user);
      });
  }

  /** Create a premium subscription for the user */
  createSubscription(card: object): Observable<void> {
    return this.apiClient.post('/api/customer/subscription', card)
      .catch(this.handleError)
      .map(() => {
        let user = this.userService.getCurrentUser();
        (<Customer>user).premium = true;
        this.userService.setCurrentUser(user);
      });
  }

  /** Cancel a user's subcription */
  cancelSubscription(): Observable<void> {
    return this.apiClient.delete('/api/customer/subscription')
      .catch(this.handleError)
      .map(() => {
        let user = this.userService.getCurrentUser();
        (<Customer>user).premium = false;
        this.userService.setCurrentUser(user);
      });
  }

  /** Change the user's password */
  changePassword(oldPassword: string, newPassword: string): Observable<void> {
    let user = this.userService.getCurrentUser();
    let request = {
      oldPassword: oldPassword,
      newPassword: newPassword
    };
    return this.apiClient.post<void>('/api/users/' + user['id'] + '/change-password/', request);
  }

  /** Change the user's profile picture */
  changeProfilePicture(dataURL: string): Observable<Image> {
    return this.apiClient.put<Image>('/api/customer/profile-picture', new Base64Image(dataURL))
      .map((image) => {
        let user = this.userService.getCurrentUser();
        user['profileImage'] = image;
        this.userService.setCurrentUser(user);
        return image;
      });
  }

  /** Get a customer's friends */
  getFriends(id: number = null): Observable<Customer[]> {
    let endpoint = '/api/customer/friends';
    if (id != null) {
      endpoint = '/api/customer/' + id + '/friends';
    }
    return this.apiClient.get<Customer[]>(endpoint);
  }

  /** Follow or unfollow a user */
  toggleFollowing(user: any): Observable<boolean> {
    if (user.followed) {
      return this.followService.unfollow('customer', user.id);
    } else {
      return this.followService.follow('customer', user.id);
    }
  }

  private handleError(err: any, caught: Observable<any>): Observable<any> {
    // todo: show error
    let message = err['error'] ? err['error'] : err;
    console.log('error occurred while getting user information:', message);
    return Observable.throw(err);
  }
}
