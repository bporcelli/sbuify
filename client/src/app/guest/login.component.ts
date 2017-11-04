import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from "../user/user.service";
import { User } from "../user/user";
import { AuthService } from "../auth/auth.service";

@Component({
    templateUrl: './login.component.html',
})
export class LoginComponent {

  errorMessage = "";

  model = new User("", "", "customer");

  remember: boolean = false;

  constructor(private userService: UserService,
              private authService: AuthService,
              private router: Router) {}

  /**
   * Form submission handler.
   */
  onSubmit() {
    var _this = this;

    this.userService.authenticate(this.model, function (token: string) {
      _this.authService.login(token, _this.remember);
    }, function () {
      _this.errorMessage = "Username or password invalid. Please try again.";
    });
  }

}
