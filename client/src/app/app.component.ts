import { Component, OnInit } from '@angular/core';
import { UserService } from "./user/user.service";
import { Customer } from "./user/customer";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  user: Customer = null;

  constructor(public userService: UserService) {}

  ngOnInit(): void {
    this.userService.currentUser
      .subscribe((user: Customer) => this.user = user);
  }

  getWrapperClasses() {
    if ( ! this.user ) {
      return 'login p-5';
    }
  }

  getMainClasses() {
    if ( this.user ) {
      return 'col-md-9 col-xl-10 content-wrapper main-content';
    } else {
      return 'col-xs-12 col-md-6 col-lg-5 col-xl-4 mx-auto';
    }
  }
}
