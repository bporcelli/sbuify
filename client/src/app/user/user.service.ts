import { Injectable } from '@angular/core';

import { APIClient } from '../api/api-client.service';
import { User } from "./user";
import { UserEndpoints } from "./endpoints";

@Injectable()
export class UserService {

  constructor(private http: APIClient) {}

  /**
   * Create (register) a new user.
   */
  public register(user: User, success: Function, error: Function) {
    this.http.post(UserEndpoints.REGISTER, user, { observe: 'response' }).subscribe(
      resp => {
        if (resp.status == 201) {
          success();
        } else {
          error();
        }
      },
      err => {
        error();
      }
    );
  }

  /**
   * Authenticate a user.
   */
  public authenticate(user: User, success: Function, error: Function) {
    this.http.post(UserEndpoints.LOGIN, user, {
      observe: 'response',
      responseType: 'text'  // workaround for Angular issue 18160
    }).subscribe(
      resp => {
        success(resp.headers.get('Authorization'));
      },
      err => {
        error();
      }
    );
  }

  /**
   * Send a password reset link to the user with the given email.
   */
  public sendResetLink(email: string, success: Function, error: Function) {
    let data = { 'email':  email };

    this.http.postAsForm(UserEndpoints.RESET_PASSWORD, data).subscribe(
      resp => {
        success();
      },
      err => {
        error(err.status);
      }
    );
  }

  /**
   * Reset a user's password.
   */
  public resetPassword(token: string, password: string, success: Function, error: Function) {
    let data = { 'token': token, 'password': password };

    this.http.postAsForm(UserEndpoints.CHANGE_PASSWORD, data).subscribe(
      resp => {
        success();
      },
      err => {
        error(err.status);
      }
    )
  }
}
