import { Component, OnInit } from '@angular/core';

import { UserService } from "./user/user.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  isAuthed: boolean = false;

  constructor(public userService: UserService) {}

  ngOnInit(): void {
    this.userService.isAuthenticated
      .subscribe((authenticated: boolean) => this.isAuthed = authenticated);
  }

  getWrapperClasses() {
    if ( ! this.isAuthed ) {
      return 'login p-5';
    }
  }

  getMainClasses() {
    if ( this.isAuthed ) {
      return 'col-md-9 col-xl-10 content-wrapper main-content';
    } else {
      return 'col-xs-12 col-md-6 col-lg-5 col-xl-4 mx-auto';
    }
  }
}
