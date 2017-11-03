import { Component, Output } from '@angular/core';

import { config } from '../common/config';

import { Customer } from "../user/customer";

@Component({
    templateUrl: './register.component.html',
})
export class RegisterComponent {

  // Error message
  errorMessage: String = "";

  // Max birthday year
  maxYear: number = new Date().getFullYear() - config['MIN_AGE'];

  // Form model
  model = new Customer("", "", "", "", new Date(this.maxYear, 0));

  @Output('verifyPassword') verifyPassword: string = "";

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

  // Process form submissions
  onSubmit() {
    // todo: inject user service; call register method to register user; navigate to login on success
    this.errorMessage = "Test";
  }
}
