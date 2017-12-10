import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormComponent } from "../shared/form.component";
import { PreferencesService } from "../user/preferences.service";
import { Customer } from "../user/customer";
import { UserService } from "../user/user.service";

@Component({
  templateUrl: './preferences.component.html'
})
export class PreferencesComponent extends FormComponent implements OnInit {

  @ViewChild(NgForm) form: NgForm;

  /** Current user */
  public user: Customer  = null;

  /** Playback quality */
  private hqStreaming: boolean = false;

  constructor(
    private prefsService: PreferencesService,
    private userService: UserService
  ) {
    super();
  }

  ngOnInit(): void {
    this.prefsService.preferences
      .subscribe((preferences: object) => {
        this.hqStreaming = preferences['hq_streaming'];
      });

    this.userService.currentUser
      .subscribe((user) => this.user = user);
  }

  onSubmit(): void {
    super.onSubmit();

    this.prefsService.setPreference('hq_streaming', this.hqStreaming)
      .subscribe(
        () => this.onSuccess(),
        (err: any) => this.onError(err)
      );
  }

  get hq(): boolean {
    return this.user != null && this.user.premium ? this.hqStreaming : false;
  }

  set hq(hq: boolean) {
    this.hqStreaming = hq;
  }

  private onSuccess() {
    this.disabled = false;
    this.form.form.markAsUntouched();
    this.form.form.markAsPristine();
  }

  private onError(err: any) {
    this.disabled = false;
    this.showFeedback(err['error'] ? err['error'] : err);
  }
}
