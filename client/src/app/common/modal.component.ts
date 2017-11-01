import { AfterViewInit, OnDestroy, ViewChild } from '@angular/core';

import { Router } from '@angular/router';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

export class ModalComponent implements AfterViewInit, OnDestroy {
    @ViewChild('content') template;

    modal: any;

    constructor(protected modalService: NgbModal, protected router: Router) {}

    ngAfterViewInit() {
        // wait one tick to avoid unidirectional-data-flow-violation-error
        // see https://angular.io/guide/component-interaction#parent-calls-an-viewchild
        setTimeout(() => this.open());
    }

    ngOnDestroy() {
        this.modal.close();
    }

    open() {
        this.modal = this.modalService.open(this.template);
       
        this.modal.result.then(() => {
            // closed
            this.close();
        }, () => {
            // dismissed
            this.close();
        })
    }

    close() {
        // remove secondary route when modal is closed
        this.router.navigate([{ outlets: { modal: null } }]);
    }
}
