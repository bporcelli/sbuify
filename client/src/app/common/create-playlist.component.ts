import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ModalComponent } from './modal.component';

@Component({
    templateUrl: './create-playlist.component.html'
})
export class CreatePlaylistComponent extends ModalComponent {
    defaultImageURL: String = 'assets/img/playlist-placeholder.jpg';

    constructor(protected modalService: NgbModal, protected router: Router) {
        super(modalService, router);
    }

    onFileChange(event, preview) {
        let file: File = event.target.files[0];

        if ( file && /^image\/*/.test(file.type) ) {  /* file is image */
            let reader = new FileReader();

            reader.onloadend = function(event) {
                preview.src = reader.result;
            }

            reader.readAsDataURL(file);
        } else {
            preview.src = this.defaultImageURL;
        }
    }
}
