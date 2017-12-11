import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgForm } from '@angular/forms';
import { FormComponent } from "../shared/form.component";
import { Config } from "../config";
import { UserService } from "./user.service";

@Component({
  templateUrl: './profile-picture-modal.component.html'
})
export class ProfilePictureModalComponent extends FormComponent implements OnInit {

  /** Selected image */
  imageURL: string = Config.DEFAULT_PLAYLIST_IMG;

  constructor(
    private activeModal: NgbActiveModal,
    private userService: UserService
  ) {
    super();
  }

  ngOnInit(): void {
    // disable submit until image is changed
    this.disabled = true;
  }

  onSubmit(): void {
    let imageURL = this.imageURL;

    if (!imageURL.startsWith('data:')) { // image was reset
      imageURL = '';
    }

    this.userService.changeProfilePicture(imageURL)
      .take(1)
      .subscribe((image) => {
        this.close(image);
      });
  }

  onFileChange(event) {
    let this$ = this;
    let file: File = event.target.files[0];

    if ( file && /^image\/*/.test(file.type) ) {  /* file is image */
      let reader = new FileReader();

      reader.onloadend = function(event) {
        this$.imageURL = reader.result;
      };

      reader.readAsDataURL(file);
    } else {
      this.imageURL = Config.DEFAULT_PLAYLIST_IMG;
    }

    this.disabled = false;
  }

  resetImage(): void {
    this.imageURL = Config.DEFAULT_PLAYLIST_IMG;
    this.disabled = false;
  }

  doSubmit(form: NgForm): void {
    form.ngSubmit.emit();
  }

  close(reason: any = 'dismissed'): void {
    this.activeModal.close(reason);
  }
}
