import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RoyaltiesService } from "./royalties.service";
import { FormComponent } from "../shared/form.component";

@Component({
  templateUrl: './royalty-modal.component.html'
})
export class RoyaltyModalComponent extends FormComponent {

  /** Payment being viewed */
  private payment: any = null;

  constructor(
    private modal: NgbActiveModal,
    private royaltyService: RoyaltiesService
  ) {
    super();
  }

  close(reason: any = null) {
    this.modal.close(reason);
  }

  markPaid(): void {
    super.onSubmit();

    this.royaltyService.markPaid(this.payment)
      .subscribe(
        () => this.close('paid'),
        (err: any) => this.onError(err)
      );
  }

  private onError(err: any) {
    this.disabled = false;
    this.showFeedback(err['error'] ? err['error'] : err);
  }
}
