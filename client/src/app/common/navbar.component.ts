import { Component } from '@angular/core';

import { Router } from '@angular/router';

import { AuthService } from '../auth/auth.service';
import { SearchService } from "../search/search.service";

@Component({
    selector: 'navbar',
    templateUrl: './navbar.component.html'
})
export class NavbarComponent {

  constructor(private auth: AuthService,
              private router: Router,
              private service: SearchService) {}

  logout() {
    this.auth.logout();
  }

  search() {
    this.router.navigate(['search'])
  }

  get query(): string {
    return this.service.getQuery().getValue();
  }

  set query(value: string) {
    this.service.setQuery(value.trim());
  }
}
