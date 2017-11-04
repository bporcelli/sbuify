import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User } from "./user";
import { UserEndpoints } from "./endpoints";

@Injectable()
export class UserService {

  constructor(private http: HttpClient) {}

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

}
