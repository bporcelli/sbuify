import { Component } from '@angular/core';

import { AuthService } from './auth/auth.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
})
export class AppComponent {
    constructor(private authService: AuthService) {}

    getWrapperClasses() {
        if ( ! this.authService.isAuthed() ) {
            return 'login p-5';
        }
    }

    getMainClasses() {
        if ( this.authService.isAuthed() ) {
            return 'col-md-9 col-xl-10 content-wrapper main-content';
        } else {
            return 'col-xs-12 col-md-6 col-lg-5 col-xl-4 mx-auto';
        }
    }
}
