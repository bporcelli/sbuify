import { Component } from '@angular/core';

import { Router } from '@angular/router';

import { AuthService } from '../auth/auth.service';

@Component({
    selector: 'navbar',
    templateUrl: './navbar.component.html'
})
export class NavbarComponent {
    constructor(private auth: AuthService,
                private router: Router) {}

    logout(e) {
        e.preventDefault();
        this.auth.logout();
    }

    search() {
        this.router.navigate(['search/songs'])
    }
}
