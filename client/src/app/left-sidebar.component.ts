import { Component, OnInit, OnDestroy } from '@angular/core';
import { UserService } from "./user/user.service";
import { User } from "./user/user";

@Component({
  selector: 'left-sidebar',
  templateUrl: './left-sidebar.component.html',
})
export class LeftSidebarComponent implements OnInit, OnDestroy {
  /** Current user */
  user: User = null;

  /** User subscription */
  private sub: any = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.sub = this.userService.currentUser
      .subscribe((user) => this.user = user);
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
