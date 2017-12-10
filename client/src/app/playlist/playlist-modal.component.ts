import { Component, Input, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormComponent } from "../shared/form.component";
import { Config } from "../config";
import { Base64Image } from "../shared/base64-image";
import { Image } from "../shared/image";
import { PlaylistService } from "./playlist.service";

@Component({
  templateUrl: './playlist-modal.component.html'
})
export class PlaylistModalComponent extends FormComponent {

  @ViewChild("form") form: NgForm;

  /** Playlist ID (if editing) */
  @Input() public id: number = 0;

  /** Folder to save playlist in */
  @Input() public folder: object = null;

  /** Name */
  @Input() public name: string = "";

  /** Description */
  @Input() public description: string = "";

  /** Image */
  @Input() private imageURL: string = Config.DEFAULT_PLAYLIST_IMG;

  /** Playlist visibility */
  @Input() private hidden: boolean = true;

  constructor(
    private activeModal: NgbActiveModal,
    private playlistService: PlaylistService
  ) {
    super();
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
  }

  /** Trigger form submission. */
  doSubmit(form: any): void {
    form.ngSubmit.emit();
  }

  /** Handle form submissions. */
  onSubmit(): void {
    super.onSubmit();

    // construct playlist
    let playlist = {
      type: 'playlist',  // type info, required for deserialization
      name: this.name,
      description: this.description,
      hidden: this.hidden
    };

    if (this.id) {
      playlist['id'] = this.id;
    }

    if (this.imageURL.startsWith('data:')) {   // new image
      playlist['image'] = new Base64Image(this.imageURL);
    } else if(this.imageURL != Config.DEFAULT_PLAYLIST_IMG) {  // keep old image
      playlist['image'] = new Image(this.id, 0, 0);
    }

    if (this.id) {  // edit
      this.playlistService.update(playlist)
        .subscribe(
          (resp: any) => this.activeModal.close(),
          (err: any) => this.handleError(err)
        );
    } else {
      this.playlistService.create(playlist, this.folder)
        .subscribe(
          (resp: any) => this.activeModal.close(),
          (err: any) => this.handleError(err)
        );
    }
  }

  /** Close the modal. */
  close(): void {
    if (this.form.form.dirty) {  // form has been modified -- warn user about data loss
      if (!confirm('Heads up! Your input will be lost.')) {
        return;
      }
    }
    this.activeModal.close();
  }

  get isPublic(): boolean {
    return !this.hidden;
  }

  @Input()
  set isPublic(isPub: boolean) {
    this.hidden = !isPub;
  }

  private handleError(err: any) {
    this.disabled = false;
    this.showFeedback(err['error'] ? err['error'] : err);
  }
}
