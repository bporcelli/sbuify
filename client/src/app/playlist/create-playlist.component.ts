import { Component, Input, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormComponent } from "../common/forms/form.component";
import { Config } from "../config";
import { Base64Image } from "../common/base64-image";
import { PlaylistService } from "./playlist.service";

@Component({
  templateUrl: './create-playlist.component.html'
})
export class CreatePlaylistComponent extends FormComponent {

  @ViewChild("createForm") form: NgForm;

  /** Name */
  public name: string = "";

  /** Description */
  public description: string = "";

  /** Image */
  private imageURL: string = "";

  /** Playlist visibility */
  private hidden: boolean = true;

  constructor(
    private activeModal: NgbActiveModal,
    private playlistService: PlaylistService
  ) {
    super();
  }

  onFileChange(event, preview) {
    let this$ = this;
    let file: File = event.target.files[0];

    if ( file && /^image\/*/.test(file.type) ) {  /* file is image */
      let reader = new FileReader();

      reader.onloadend = function(event) {
        preview.src = reader.result;
        this$.imageURL = preview.src;
      };

      reader.readAsDataURL(file);
    } else {
      preview.src = Config.DEFAULT_PLAYLIST_IMG;
    }
  }

  /** Trigger form submission. */
  doSubmit(form: any): void {
    form.ngSubmit.emit();
  }

  /** Handle form submissions. */
  onSubmit(): void {
    // construct playlist
    let playlist = {
      type: 'playlist',  // type info, required for deserialization
      name: this.name,
      description: this.description,
      hidden: this.hidden,
      image: new Base64Image(this.imageURL)
    };

    // create
    this.playlistService.create(playlist)
      .subscribe(
        (resp: any) => this.activeModal.close(),
        (err: any) => this.handleError(err)
      );
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
