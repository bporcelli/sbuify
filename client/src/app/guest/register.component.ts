import { Component, Output } from '@angular/core';
import { Router } from '@angular/router';

import { Config } from '../config';

import { Customer } from "../user/customer";
import { UserService } from "../user/user.service";

@Component({
    templateUrl: './register.component.html',
})
export class RegisterComponent {

  // Error message
  errorMessage: String = "";

  // Max birthday year
  maxYear: number = this.getMaxBirthYear();

  // Form model
  model = this.getDefaultCustomer();

  @Output('verifyPassword') verifyPassword: string = "";

  constructor(private userService: UserService,
              private router: Router) {}

  /**
   * Handle form submissions.
   */
  onSubmit() {
    let _this = this;

    this.errorMessage = "";

    this.userService.register(this.model, function() {
      // TODO: pass special route param so we can display a success message on the login page
      _this.router.navigate(['/login']);
    }, function() {
      _this.errorMessage = "Your email address is already in use. Please try again.";
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
