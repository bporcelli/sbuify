import { Component, ViewChild, Input } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PlaylistService } from "./playlist.service";
import { FormComponent } from "../common/form.component";

@Component({
  templateUrl: './playlist-folder-modal.component.html'
})
export class PlaylistFolderModalComponent extends FormComponent {

  @ViewChild('form') form: NgForm;

  /** ID of folder if folder is being edited */
  @Input() public id: number = 0;

  /** Folder name */
  @Input() public name: string = '';

  constructor(
    private activeModal: NgbActiveModal,
    private playlistService: PlaylistService
  ) {
    super();
  }

  /** Trigger form submission. */
  doSubmit(form: any): void {
    form.ngSubmit.emit();
  }

  /** Handle form submissions. */
  onSubmit(): void {
    let folder = {
      name: this.name,
      folder: true
    };

    if (this.id) {
      folder['id'] = this.id;

      this.playlistService.update(folder)
        .subscribe(
          () => this.activeModal.close(),
          (err: any) => this.handleError(err)
        );
    } else {
      this.playlistService.create(folder)
        .subscribe(
          () => this.activeModal.close(),
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

  private handleError(err: any) {
    this.showFeedback(err['error'] ? err['error'] : err);
  }
}
