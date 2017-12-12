import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormComponent } from "../shared/form.component";
import { User } from "../user/user";
import { UserService } from "../user/user.service";
import { Customer } from "../user/customer";
import { Admin } from "./admin";
import {Config} from "../config";

@Component({
  templateUrl: './create-user.component.html'
})
export class CreateUserComponent extends FormComponent {

  @ViewChild(NgForm) form: NgForm;

  /** Form model (initial user type is customer) */
  private user: User = new Customer('', '', '', '', new Date(this.maxYear, 0, 1));

  /** Password confirmation */
  private passConfirmation: string = '';

  constructor(private userService: UserService) {
    super();
  }

  onSubmit(): void {
    super.onSubmit();

    this.userService.create(this.user)
      .subscribe(
        (user: User) => this.onSuccess(),
        (err: any) => this.onError(err)
      );
  }

  onTypeChanged(newType: string): void {
    let cur = this.user;

    if (newType == 'admin') {
      this.user = new Admin(cur['email'], cur['password'], cur['firstName'], cur['lastName'], false);
    } else if (newType == 'customer') {
      this.user = new Customer(cur['email'], cur['password'], cur['firstName'], cur['lastName']);
    }
  }

  get birthMonth(): any {
    return this.user.type == 'customer' ? this.user['birthday'].getMonth() : '';
  }

  set birthMonth(month: any) {
    if (month == null || this.user.type != 'customer') {
      return;
    }
    this.user['birthday'].setMonth(month);
  }

  get birthDate(): any {
    return this.user.type == 'customer' ? this.user['birthday'].getDate() : '';
  }

  set birthDate(date: any) {
    if (date == null || this.user.type != 'customer') {
      return;
    }
    this.user['birthday'].setDate(date);
  }

  get birthYear(): any {
    return this.user.type == 'customer' ? this.user['birthday'].getFullYear() : '';
  }

  set birthYear(year: any) {
    if (year == null || this.user.type != 'customer') {
      return;
    }
    this.user['birthday'].setFullYear(year);
  }

  get minYear(): any {
    return new Date().getFullYear() - Config.MAX_AGE;
  }

  get maxYear(): any {
    return new Date().getFullYear() - Config.MIN_AGE;
  }

  private onSuccess() {
    this.disabled = false;
    this.form.resetForm();
    this.showFeedback("User created successfully.", "success");
  }

  private onError(err: any) {
    this.disabled = false;
    this.showFeedback(err['error'] ? err['error'] : err);
  }
}
