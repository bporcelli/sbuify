import { NgModule } from '@angular/core';
import { Http, RequestOptions } from '@angular/http';
import { AuthHttp, AuthConfig } from 'angular2-jwt';

import { AuthService } from './auth.service';
import { AuthGuard } from "./auth-guard.service";
import { tokenGetter } from "./helpers";

export function authHttpServiceFactory(http: Http, options: RequestOptions) {
  let config = {
    tokenGetter: tokenGetter()
  };
  return new AuthHttp(new AuthConfig(config), http, options);
}

@NgModule({
  providers: [
    AuthService,
    AuthGuard,
    {
      provide: AuthHttp,
      useFactory: authHttpServiceFactory,
      deps: [Http, RequestOptions]
    }
  ]
})
export class AuthModule {}
