import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormComponent } from "../shared/form.component";
import { User } from "../user/user";
import { UserService } from "../user/user.service";
import { Admin } from "./admin";
import { Customer } from "../user/customer";

@Component({
  templateUrl: './edit-user-modal.component.html'
})
export class EditUserModalComponent extends FormComponent {

  @ViewChild(NgForm) form: NgForm;

  /** A modifiable copy of the user being edited */
  private _user: User = null;

  constructor(
    private modal: NgbActiveModal,
    private userService: UserService
  ) {
    super();
  }

  onSubmit(): void {
    super.onSubmit();

    this.userService.update(this._user)
      .subscribe(
        (updated: User) => this.close(updated),
        (err: any) => this.onError(err)
      );
  }

  doSubmit(): void {
    this.form.ngSubmit.emit();
  }

  close(reason: any = 'dismissed'): void {
    this.modal.close(reason);
  }

  get user(): any {
    return this._user;
  }

  set user(user: any) {
    if (user.type == 'admin') {
      this._user = new Admin(user.email, user.password, user.firstName, user.lastName, user.superAdmin);

    } else if (user.type == 'customer') {
      this._user = new Customer(user.email, user.password, user.firstName, user.lastName, user.birthday);
    }
    this._user.id = user.id;
  }

  private onError(err: any) {
    this.disabled = false;
    this.showFeedback(err['error'] ? err['error'] : err);
  }
}
