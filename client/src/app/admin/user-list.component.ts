import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { User } from "../user/user";
import { UserService } from "../user/user.service";
import { Config } from "../config";
import { EditUserModalComponent } from "./edit-user-modal.component";

@Component({
  templateUrl: './user-list.component.html'
})
export class UserListComponent implements OnInit, OnDestroy {

  /** Current user */
  private current: User = null;
  private sub: any = null;

  /** Unfiltered users list */
  private unfiltered: User[] = [];

  /** Current filter value */
  private filter: string = '';

  /** Filtered users */
  private users: User[] = [];

  /** Current page */
  private page: number = 0;

  /** Are more users available? */
  private more: boolean = true;

  /** Is a request for more users pending? */
  private pending: boolean = false;

  /** Bound version of isNotUser -- needed to access this */
  public isNotUserBound = this.isNotUser.bind(this);

  constructor(
    private userService: UserService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.nextPage();

    this.sub = this.userService.currentUser
      .subscribe((user) => this.current = user);
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  editUser(user: User): void {
    let modal = this.modalService.open(EditUserModalComponent);

    modal.componentInstance.user = user;

    modal.result.then((reason: any) => {
      if (reason != 'dismissed') {
        let index = this.unfiltered.findIndex(value => value.id == user.id);
        this.unfiltered[index] = reason;
        this.onFilterChange();
      }
    });
  }

  deleteUser(user: User): void {
    this.userService.delete(user)
      .subscribe((deleted) => {
        if (deleted) {
          const temp = this.unfiltered.filter(value => value['id'] != user['id']);
          this.unfiltered = temp;
          this.onFilterChange();
        }
      });
  }

  isNotUser(user: User): boolean {
    return this.current != null && this.current['id'] != user['id'];
  }

  private nextPage() {
    if (!this.more) {
      return;
    } else if (this.pending) {
      return;
    }

    this.pending = true;

    this.userService.getAll(this.page)
      .take(1)
      .subscribe(
        (users: User[]) => this.handleNewUsers(users),
        (err: any) => this.handleError(err)
      );
  }

  private handleNewUsers(users: User[]) {
    this.pending = false;

    this.unfiltered.push(...users);
    this.onFilterChange();
    this.page++;

    if (users.length < Config.ITEMS_PER_PAGE) {
      this.more = false;
    }
  }

  private onFilterChange(value: any = null) {
    if (value == null) {  // reuse previous filter value
      value = this.filter;
    }

    if (!value) {
      this.users = this.unfiltered;
    } else {
      const temp = this.unfiltered.filter(user => {
        let fields = ['name', 'type', 'email', 'id'];
        for (let field of fields) {
          let userVal = user[field];
          if (typeof user[field] == 'number') {
            userVal = userVal.toString();
          }
          if (userVal.indexOf(value) !== -1) {
            return true;
          }
        }
        return false;
      });
      this.users = temp;
    }

    this.filter = value;
  }

  private handleError(err: any) {
    // todo: show error
    console.log('error occurred while getting users:', err);
  }
}
