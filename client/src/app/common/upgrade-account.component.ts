import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ModalComponent } from './modal.component';

@Component({
    templateUrl: './upgrade-account.component.html'
})
export class UpgradeAccountComponent extends ModalComponent {
    constructor(protected modalService: NgbModal, protected router: Router) {
        super(modalService, router);
    }
}
