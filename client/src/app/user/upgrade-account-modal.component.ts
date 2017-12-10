import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormComponent } from "../shared/form.component";
import { Config } from "../config";
import { UserService } from "./user.service";

@Component({
  templateUrl: './upgrade-account-modal.component.html'
})
export class UpgradeAccountModalComponent extends FormComponent {

  @ViewChild(NgForm) form: NgForm;

  /** Expiration year */
  expYear: string = this.validYears[0].toString();

  /** Credit card number */
  cardNumber: string = '';

  /** CVV/CVC */
  cvc: string = '';

  /** Expiration month */
  expMonth: string = '01';

  constructor(
    private activeModal: NgbActiveModal,
    private userService: UserService
  ) {
    super();
  }

  close(): void {
    this.activeModal.close();
  }

  doSubmit(): void {
    this.form.ngSubmit.emit();
  }

  onSubmit(): void {
    super.onSubmit();

    let card = {
      cardNumber: this.cardNumber,
      expMonth: this.expMonth,
      expYear: this.expYear,
      cvc: this.cvc
    };

    this.userService.createSubscription(card)
      .subscribe(
        () => this.close(),
        (err: any) => this.handleError(err)
      );
  }

  private handleError(err: any) {
    this.disabled = false;
    this.showFeedback(err['error'] ? err['error'] : err);
  }

  get validYears(): number[] {
    let res = [];
    let year = new Date().getFullYear();
    for (let i = 0; i < Config.MAX_CC_AGE; i++) {
      res.push(year + i);
    }
    return res;
  }
}
