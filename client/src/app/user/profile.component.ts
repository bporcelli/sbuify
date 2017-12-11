import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Customer } from "./customer";
import { UserService } from "./user.service";
import {ProfilePictureModalComponent} from "./profile-picture-modal.component";
import {Image} from "../shared/image";

@Component({
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  /** Current user */
  current: Customer = null;

  /** User being viewed */
  user: Customer = null;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.route.data
      .subscribe((data: { user: Customer }) => this.user = data.user);

    this.userService.currentUser.take(1)
      .subscribe((current) => this.current = current);
  }

  /** Open Profile Picture modal */
  openPictureModal(): void {
    if (!this.isCurrentUser()) {
      return;
    }

    let modal = this.modalService.open(ProfilePictureModalComponent);

    if (this.user['profileImage'] != null) {
      modal.componentInstance.imageURL = '/static/i/' + this.user['profileImage']['id'] + '/catalog';
    }

    modal.result.then((reason: any) => {
      if (reason != 'dismissed') {
        this.user['profileImage'] = reason;
      }
    });
  }

  /** Follow or unfollow the visible customer */
  toggleFollowing(): void {
    this.userService.toggleFollowing(this.user)
      .subscribe((following) => this.user['followed'] = following);
  }

  isCurrentUser(): boolean {
    console.log('checking: is', this.user, '=', this.current);

    return this.current && this.user && this.current['id'] == this.user['id'];
  }
}
