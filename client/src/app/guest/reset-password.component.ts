import { Component, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormComponent } from "../common/form.component";
import { UserEndpoints } from "../user/endpoints";
import { APIClient } from "../common/api-client.service";

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
              private http: APIClient) {
    super();
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('t');
  }

  /** Form submission handler. */
  onSubmit(): void {
    super.onSubmit();

    if (this.token) {
      this.changePassword();
    } else {
      this.sendResetLink();
    }
  }

  /** Request password reset link. */
  sendResetLink() {
    let $this = this;

    let data = { 'email':  this.email };

    this.http.postAsForm(UserEndpoints.RESET_PASSWORD, data)
      .subscribe(
        resp => {
          $this.showFeedback("Success! Please check your inbox for further instructions.", "success");
        },
        err => {
          $this.handleResetError(err.status);
        });
  }

  private handleResetError(status: number) {
    let message = "An error occurred. Please try again.";
    if (404 == status) {
      message = "Invalid email address.";
    } else if (500 == status) {
      message = "Email delivery failed.";
    }
    this.showFeedback(message);
    this.disabled = false;
  }

  /** Update the user's password */
  changePassword() {
    let $this = this;

    let data = { 'token': this.token, 'password': this.password };

    this.http.postAsForm(UserEndpoints.CHANGE_PASSWORD, data).subscribe(
      resp => {
        $this.router.navigate(['/login', { message: 'reset-success' }]);
      },
      err => {
        $this.handleChangePassError(err.status);
      });
  }

  private handleChangePassError(status: number) {
    let message = "An error occurred. Please try again.";
    if (status == 404) {
      message = "Invalid reset token. Did you enter your password reset link correctly?"
    }
    this.showFeedback(message);
    this.disabled = false;
  }

  /** Submit button text. */
  get buttonText(): string {
    return this.token ? 'Change Password' : 'Send Reset Link';
  }
}
