import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { UserService } from "../user/user.service";
import { User } from "../user/user";
import { AuthService } from "../auth/auth.service";
import { FormComponent } from "../common/forms/form.component";

@Component({
    templateUrl: './login.component.html',
})
export class LoginComponent extends FormComponent implements OnInit {

  /** Form model */
  model: User = new User("", "", "customer");

  /** 'Remember me' flag */
  remember: boolean = false;

  constructor(private userService: UserService,
              private authService: AuthService,
              private route: ActivatedRoute) {
    super();
  }

  /**
   * Form submission handler.
   */
  onSubmit() {
    var $this = this;

    this.userService.authenticate(this.model, function (token: string) {
      $this.authService.login(token, $this.remember);
    }, function () {
      $this.showFeedback("Username or password invalid. Please try again.");
    });
  }

  /**
   * Optionally show a success message when the component is initialized.
   */
  ngOnInit(): void {
    let params = this.route.snapshot.paramMap;
    let message = params.get('message');
    
    if ('reset-success' == message) {
      this.showFeedback("Password changed successfully.", "success");
    } else if ('register-success' == message) {
      this.showFeedback("Account created successfully.", "success");
    }
  }
}
