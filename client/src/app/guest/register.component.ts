import { Component, Output } from '@angular/core';
import { Router } from '@angular/router';

import { Config } from '../config';

import { Customer } from "../user/customer";
import { UserService } from "../user/user.service";
import { FormComponent } from "../common/forms/form.component";

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

  constructor(private userService: UserService,
              private router: Router) {
    super();
  }

  /**
   * Handle form submissions.
   */
  onSubmit() {
    super.onSubmit();

    let $this = this;

    this.userService.register(this.model, function() {
      $this.router.navigate(['/login', { message: 'register-success' }]);
    }, function() {
      $this.showFeedback("That email address is in use. Please try again.");
      $this.disabled = false;
    });
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
