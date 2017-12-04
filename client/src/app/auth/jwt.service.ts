import { Injectable } from '@angular/core';
import { tokenGetter } from "./helpers";
import { JwtHelper } from "angular2-jwt/angular2-jwt";

@Injectable()
export class JwtService {

  jwtHelper: JwtHelper = new JwtHelper();

  setToken(token: string, remember: boolean) {
    localStorage.setItem('remember', JSON.stringify(remember));

    if (remember) {
      localStorage.setItem('token', token);
    } else {
      sessionStorage.setItem('token', token);  // expires after page is closed
    }
  }

  getToken(): string {
    return tokenGetter()();
  }

  getDecodedToken(): any {
    return this.decodeToken(this.getToken());
  }

  clearToken(): void {
    localStorage.removeItem('remember');
    localStorage.removeItem('token');
    sessionStorage.removeItem('token');
  }

  hasToken(): boolean {
    let token = this.getToken();
    return token && !this.jwtHelper.isTokenExpired(token);
  }

  decodeToken(token: string): any {
    return this.jwtHelper.decodeToken(token);
  }
}
