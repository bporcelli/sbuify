import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Customer } from "./customer";
import { UserService } from "./user.service";
import { ProfilePictureModalComponent } from "./profile-picture-modal.component";
import { CustomerService } from "./customer.service";

@Component({
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  /** Current user */
  private current: Customer = null;

  /** User being viewed */
  private user: Customer = null;

  /** Friends */
  private friends: Customer[] = [];

  /** Are the friends still loading? */
  private loading: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private customerService: CustomerService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.route.data
      .subscribe((data: { user: Customer }) => this.init(data.user));

    this.userService.currentUser.take(1)
      .subscribe((current) => this.current = <Customer>current);
  }

  /** Initialize user and friends */
  private init(user: Customer) {
    this.user = user;

    this.customerService.getFriends(this.user['id'])
      .subscribe((friends) => {
        this.friends = friends;
        this.loading = false;
      });
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
    this.customerService.toggleFollowing(this.user)
      .subscribe((following) => this.user['followed'] = following);
  }

  /** Unfollow a friend */
  removeFriend(friend: any): void {
    this.customerService.toggleFollowing(friend)
      .subscribe(() => {
        this.friends = this.friends.filter(value => value['id'] != friend['id']);
      });
  }

  isCurrentUser(): boolean {
    return this.current && this.user && this.current['id'] == this.user['id'];
  }
}
