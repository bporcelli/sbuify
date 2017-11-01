import { Component } from '@angular/core';

import { Router } from '@angular/router';

import { AuthService } from '../auth.service';

@Component({
    selector: 'navbar',
    templateUrl: './navbar.component.html'
})
export class NavbarComponent {
    constructor(private authService: AuthService,
                private router: Router) {}

    logout(e) {
        e.preventDefault();

        this.authService.logout();
        this.router.navigate(['login']);
    }

    search() {
        this.router.navigate(['search/songs'])
    }
}