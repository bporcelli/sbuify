import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SearchService } from "../search/search.service";
import { UserService } from "../user/user.service";

@Component({
    selector: 'navbar',
    templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit {

  isAuthed: boolean = false;

  constructor(
    public userService: UserService,
    private searchService: SearchService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userService.isAuthenticated
      .subscribe((authenticated) => this.isAuthed = authenticated);
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
}
