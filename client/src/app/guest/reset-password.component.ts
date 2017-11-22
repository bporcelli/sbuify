import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from "../user/user.service";

class FormFeedback {
  type: String;
  text: String;

  constructor(text: String, type: String = "error") {
    this.type = type;
    this.text = text;
  }

  get class(): String {
    return "form-feedback " + this.type;
  }
}

@Component({
    templateUrl: './reset-password.component.html',
})
export class ResetPasswordComponent implements OnInit {

  token: string = "";  // password reset token
  email: string = "";
  password: string = "";
  passwordConfirmation: string = "";

  feedback: FormFeedback = null;
  disabled: boolean = false;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private userService: UserService) {}

  /**
   * Set the password reset token on initialization.
   */
  ngOnInit(): void {
    let params = this.route.snapshot.queryParamMap;
    this.token = params.get('t');
  }

  /**
   * Form submission handler.
   */
  onSubmit(): void {
    this.feedback = null;
    this.disabled = true;

    if (this.token) {
      this.changePassword();
    } else {
      this.sendResetLink();
    }
  }

  /**
   * Request password reset link.
   */
  sendResetLink() {
    let _this = this;

    this.userService.sendResetLink(this.email, function() {
      _this.feedback = new FormFeedback("Success! Please check your inbox for further instructions.", "success");
    }, function(status: number) {
      let message = "An error occurred. Please try again.";

      if (404 == status) {
        message = "Invalid email address.";
      } else if (500 == status) {
        message = "Email delivery failed.";
      }
      _this.feedback = new FormFeedback(message);
      _this.disabled = false;
    });
  }

  /**
   * Update the user's password.
   */
  changePassword() {
    let _this = this;

    this.userService.resetPassword(this.token, this.password, function() {
      _this.router.navigate(['/login']); // todo: show message on login page
    }, function(status: number) {
      let message = "An error occurred. Please try again.";

      if (status == 404) {
        message = "Invalid reset token. Did you enter your password reset link correctly?"
      }
      _this.feedback = new FormFeedback(message);
      _this.disabled = false;
    });
  }

  /**
   * Submit button text.
   */
  get buttonText(): string {
    return this.token ? 'Change Password' : 'Send Reset Link';
  }
}
