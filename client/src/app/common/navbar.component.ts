import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SearchService } from "../search/search.service";
import { UserService } from "../user/user.service";
import { User } from "../user/user";

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit {
  user: User = null;

  constructor(
    public userService: UserService,
    private searchService: SearchService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userService.currentUser
      .subscribe((user) => this.user = user);
  }

  logout() {
    this.userService.clearAuth();
    this.router.navigate(['/login']);
  }

  search() {
    this.router.navigate(['search'])
  }

  get query(): string {
    return this.searchService.getQuery().getValue();
  }

  set query(value: string) {
    this.searchService.setQuery(value.trim());
  }

  get isAuthed(): boolean {
    return this.user != null;
  }
}
