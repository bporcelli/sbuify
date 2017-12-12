import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormComponent } from "../shared/form.component";
import { CustomerService } from "../user/customer.service";

@Component({
  templateUrl: './change-password.component.html'
})
export class ChangePasswordComponent extends FormComponent {

  @ViewChild(NgForm) form: NgForm;

  /** Old pass */
  oldPassword: string = '';

  /** New pass */
  newPassword: string = '';

  /** Confirmation */
  confirmPassword: string = '';

  constructor(private customerService: CustomerService) {
    super();
  }

  onSubmit(): void {
    super.onSubmit();

    this.customerService.changePassword(this.oldPassword, this.newPassword)
      .subscribe(
        () => this.onSuccess(),
        (err: any) => this.onError(err)
      );
  }

  private onSuccess() {
    this.disabled = false;
    this.form.reset();
  }

  private onError(err: any) {
    this.disabled = false;
    this.showFeedback(err['error'] ? err['error'] : err);
  }
}
