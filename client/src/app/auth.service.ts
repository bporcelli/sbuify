import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';

import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';

@Injectable()
export class AuthService {
    authed = false;

    // URL to redirect to after login
    redirectURL: string;

    isAuthed() {
        return this.authed;
    }

    login(): Observable<boolean> {
        return Observable.of(true).delay(1000).do(val => this.authed = true);
    }

    logout() {
        this.authed = false;
    }
}