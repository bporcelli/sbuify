import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormComponent } from "../shared/form.component";
import { Config } from "../config";
import { UserService } from "../user/user.service";
import { Customer } from "../user/customer";
import { CustomerService } from "../user/customer.service";

@Component({
  templateUrl: './edit-account.component.html'
})
export class EditAccountComponent extends FormComponent implements OnInit {

  @ViewChild(NgForm) form: NgForm;

  /** User being edited */
  private user: Customer = null;

  /** Password confirmation */
  private _pass: string = '';

  constructor(
    private userService: UserService,
    private customerService: CustomerService
  ) {
    super();
  }

  ngOnInit(): void {
    this.userService.currentUser
      .filter(user => user != null)
      .subscribe((user: any) => {
        this.user = user;

        // parse birthday
        this.user.birthday = new Date(Date.parse(user['birthday']));
      });
  }

  onSubmit(): void {
    super.onSubmit();

    this.customerService.updateUser(this.user)
      .subscribe(
        () => this.onSuccess(),
        (err: any) => this.onError(err)
      )
  }

  doDowngrade(): void {
    if (confirm('Are you sure you want to cancel your subscription?')) {
      this.customerService.cancelSubscription()
        .subscribe(
          () => this.onSuccess(),
          (err: any) => this.onError(err)
        );
    }
  }

  get currentYear(): number {
    return new Date().getFullYear();
  }

  get minBirthYear(): number {
    return this.currentYear - Config.MAX_AGE;
  }

  get maxBirthYear(): number {
    return this.currentYear - Config.MIN_AGE;
  }

  get firstName(): string {
    return this.user ? this.user.firstName : '';
  }

  set firstName(first: string) {
    this.user.firstName = first;
  }

  get lastName(): string {
    return this.user ? this.user.lastName : '';
  }

  set lastName(last: string) {
    this.user.lastName = last;
  }

  get email(): string {
    return this.user ? this.user.email : '';
  }

  set email(email: string) {
    this.user.email = email;
  }

  get birthday(): Date {
    return this.user ? this.user.birthday : new Date(this.currentYear, 1, 1);
  }

  get birthMonth(): number {
    return this.birthday.getMonth() + 1;
  }

  set birthMonth(month: number) {
    if (month == null) return;
    this.user.birthday.setMonth(month - 1);
  }

  get birthDate(): number {
    return this.birthday.getDate();
  }

  set birthDate(date: number) {
    if (date == null) return;
    this.user.birthday.setDate(date);
  }

  get birthYear(): number {
    return this.birthday.getFullYear();
  }

  set birthYear(year: number) {
    if (year == null) return;
    this.user.birthday.setFullYear(year);
  }

  get password(): string {
    return this._pass;
  }

  set password(pass: string) {
    this._pass = this.user.password = pass;
  }

  private onSuccess() {
    this.disabled = false;
    this.form.form.markAsUntouched();
    this.form.form.markAsPristine();
  }

  private onError(err: any) {
    this.disabled = false;
    this.showFeedback(err['error'] ? err['error'] : err);
  }
}
