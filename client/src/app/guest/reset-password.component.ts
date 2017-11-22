import { Component, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from "../user/user.service";
import { FormComponent } from "../common/forms/form.component";

@Component({
    templateUrl: './reset-password.component.html',
})
export class ResetPasswordComponent extends FormComponent implements OnInit {

  /** Password reset token */
  token: string = "";

  /** Email address */
  email: string = "";

  /** New password */
  password: string = "";

  /** New password confirmation */
  @Output('confirmPassword') confirmPassword: string = "";

  constructor(private router: Router,
              private route: ActivatedRoute,
              private userService: UserService) {
    super();
  }

  /**
   * Set the password reset token on initialization.
   */
  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('t');
  }

  /**
   * Form submission handler.
   */
  onSubmit(): void {
    super.onSubmit();

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
    let $this = this;

    this.userService.sendResetLink(this.email, function() {
      $this.showFeedback("Success! Please check your inbox for further instructions.", "success")
    }, function(status: number) {
      let message = "An error occurred. Please try again.";

      if (404 == status) {
        message = "Invalid email address.";
      } else if (500 == status) {
        message = "Email delivery failed.";
      }
      $this.showFeedback(message);
      $this.disabled = false;
    });
  }

  /**
   * Update the user's password.
   */
  changePassword() {
    let $this = this;

    this.userService.resetPassword(this.token, this.password, function() {
      $this.router.navigate(['/login', { message: 'reset-success' }]);
    }, function(status: number) {
      let message = "An error occurred. Please try again.";

      if (status == 404) {
        message = "Invalid reset token. Did you enter your password reset link correctly?"
      }
      $this.showFeedback(message);
      $this.disabled = false;
    });
  }

  /**
   * Submit button text.
   */
  get buttonText(): string {
    return this.token ? 'Change Password' : 'Send Reset Link';
  }
}
