import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../auth.service';

@Component({
    templateUrl: './login.component.html',
})
export class LoginComponent {
    defaultRedirectURL: string = '/browse/overview';

    constructor(private authService: AuthService,
                private router: Router) {}

    login(e) {
        e.preventDefault();

        this.authService.login().subscribe(() => {
            if ( this.authService.isAuthed() ) {
                let redirect = this.authService.redirectURL ? this.authService.redirectURL : this.defaultRedirectURL;

                this.router.navigate([redirect]);
            }
        });
    }
}