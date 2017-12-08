import { Component, Output } from '@angular/core';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

import { Config } from '../config';

import { Customer } from "../user/customer";
import { FormComponent } from "../common/form.component";
import { APIClient } from "../common/api-client.service";
import { UserEndpoints } from "../user/endpoints";
import { Observable } from "rxjs/Rx";

@Component({
    templateUrl: './register.component.html',
})
export class RegisterComponent extends FormComponent {

  /** Max birthday year */
  maxYear: number = this.getMaxBirthYear();

  /** Form model */
  model = this.getDefaultCustomer();

  /** Password confirmation */
  @Output('verifyPassword') verifyPassword: string = "";

  constructor(private router: Router,
              private http: APIClient) {
    super();
  }

  /**
   * Handle form submissions.
   */
  onSubmit() {
    super.onSubmit();

    this.http.post(UserEndpoints.REGISTER, this.model, { observe: 'response' })
      .catch(this.catchErrorResponse)
      .finally(() => this.disabled = false)
      .subscribe((resp: any) => this.handleSuccess(resp));
  }

  handleSuccess(resp: any) {
    if (resp.status == 201) {
      this.router.navigate(['/login', { message: 'register-success' }]);
    } else {
      this.showFeedback("That email address is in use. Please try again.");
    }
  }

  catchErrorResponse(err: any, resp: Observable<HttpResponse<any>>): Observable<any> {
    // todo: log and handle error
    console.log('failed to register user:', err);
    return Observable.throw(err);
  }

  /**
   * Compute maximum birth year based on minimum age.
   * @returns {number}
   */
  getMaxBirthYear(): number {
    return new Date().getFullYear() - Config.MIN_AGE;
  }

  /**
   * Get the default Customer model.
   * @returns Customer
   */
  getDefaultCustomer(): Customer {
    return new Customer("", "", "", "", new Date(this.maxYear, 0));
  }

  /**
   * Getters and setters.
   */
  @Output('birthYear')
  get birthYear() {
    return this.model.birthday.getFullYear();
  }

  set birthYear(year: number) {
    if (year > this.maxYear) { // maxYear exceeded
      year = this.maxYear;
    }
    this.model.birthday.setFullYear(year);
  }

  @Output('birthMonth')
  get birthMonth() {
    return this.model.birthday.getMonth();
  }

  set birthMonth(month: number) {
    this.model.birthday.setMonth(month);
  }

  @Output('birthDate')
  get birthDate() {
    return this.model.birthday.getDate();
  }

  set birthDate(date: number) {
    this.model.birthday.setDate(date);
  }
}
