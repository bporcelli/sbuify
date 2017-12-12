import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { RoyaltiesService } from "./royalties.service";
import { Config } from "../config";
import { RoyaltyModalComponent } from "./royalty-modal.component";

@Component({
  templateUrl: './royalties.component.html'
})
export class RoyaltiesComponent implements OnInit {

  @ViewChild(NgForm) form: NgForm;

  /** Payment periods available for selection */
  private periods: any[] = null;

  /** Selected payment status */
  private status: string = 'ALL';

  /** Selected payment period */
  private period: string = '';

  /** Royalty payments retrieved from server */
  private payments: any[] = [];

  /** Current page of results */
  private page: number = 0;

  /** Is a request for more royalties pending? */
  private pending: boolean = false;

  /** Are more payments available? */
  private more: boolean = true;

  constructor(
    private royaltyService: RoyaltiesService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.royaltyService.getPaymentPeriods()
      .subscribe((periods) => this.periods = periods);

   this.nextPage();
  }

  onFiltersChanged(): void {
    // reset to first page
    this.pending = false;
    this.page = 0;
    this.more = true;
    this.payments = [];

    // load new results
    this.nextPage();
  }

  viewPayment(payment: any): void {
    let modal = this.modalService.open(RoyaltyModalComponent);

    modal.componentInstance.payment = payment;

    modal.result.then((result: any) => {
      if (result != null) {  // payment was paid
        payment['status'] = 'PAID';
      }
    });
  }

  isNotPaid(payment: any): boolean {
    return payment['status'] != 'PAID';
  }

  markPaid(payment: any): void {
    document.body.classList.add('loading');

    this.royaltyService.markPaid(payment)
      .subscribe(() => {
        payment['status'] = 'PAID';
        document.body.classList.remove('loading');
      }, (err: any) => {
        this.handleError(err)
      });
  }

  private nextPage() {
    if (!this.more || this.pending) {
      return;
    }

    this.pending = true;

    this.royaltyService.getRoyaltyPayments(this.page, this.status, this.period)
      .subscribe(
        (result: any[]) => this.handleNewPayments(result),
        (err: any) => this.handleError(err)
      )
  }

  private handleNewPayments(result: any[]) {
    this.pending = false;

    this.payments.push(...result);
    this.page++;

    if (result.length < Config.ITEMS_PER_PAGE) {
      this.more = false;
    }

    this.resetForm();
  }

  private handleError(err: any) {
    this.pending = false;
    // todo: show error
    console.log('error occurred while fetching payments:', err);
    this.resetForm();
  }

  private resetForm() {
    this.form.form.markAsPristine();
    this.form.form.markAsUntouched();
  }
}
