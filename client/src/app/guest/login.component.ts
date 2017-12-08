import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { User } from "../user/user";
import { FormComponent } from "../common/form.component";
import { APIClient } from "../common/api-client.service";
import { UserEndpoints } from "../user/endpoints";
import { UserService } from "../user/user.service";
import { Config } from "../config";

@Component({
    templateUrl: './login.component.html',
})
export class LoginComponent extends FormComponent implements OnInit {

  /** Form model */
  model: User = new User("", "", "customer");

  /** 'Remember me' flag */
  remember: boolean = false;

  /** URL to redirect to on success */
  redirectURL: string = '';

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private http: APIClient
  ) {
    super();
  }

  ngOnInit(): void {
    let params = this.route.snapshot.paramMap;
    let message = params.get('message');

    // optionally show a message
    if ('reset-success' == message) {
      this.showFeedback("Password changed successfully.", "success");
    } else if ('register-success' == message) {
      this.showFeedback("Account created successfully.", "success");
    }

    // set the redirect URL
    this.redirectURL = params.get('redirect');
  }

  private handleSuccess(resp: any) {
    this.userService.setAuth(resp.headers.get('Authorization'), this.remember);

    if ( !this.redirectURL ) {
      this.redirectURL = Config.DEFAULT_REDIRECT;
    } else {
      this.redirectURL = decodeURIComponent(this.redirectURL);
    }

    this.router.navigate([this.redirectURL]);
  }

  /** Handle form submissions */
  onSubmit() {
    this.http.post(UserEndpoints.LOGIN, this.model, {
      observe: 'response',
      responseType: 'text'  // workaround for Angular issue 18160
    }).subscribe(
      resp => this.handleSuccess(resp),
      err => this.handleError(err)
    );
  }

  private handleError(err: any) {
    this.showFeedback("Username or password invalid. Please try again.");
  }
}
