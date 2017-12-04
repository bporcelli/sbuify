import { NgModule } from '@angular/core';
import { Http, RequestOptions } from '@angular/http';
import { AuthHttp, AuthConfig } from 'angular2-jwt';
import { AuthGuard } from "./auth-guard.service";
import { tokenGetter } from "./helpers";
import { JwtService } from "./jwt.service";
import { NoAuthGuard } from "./no-auth-guard";

export function authHttpServiceFactory(http: Http, options: RequestOptions) {
  let config = {
    tokenGetter: tokenGetter()
  };
  return new AuthHttp(new AuthConfig(config), http, options);
}

@NgModule({
  providers: [
    AuthGuard,
    NoAuthGuard,
    JwtService,
    {
      provide: AuthHttp,
      useFactory: authHttpServiceFactory,
      deps: [Http, RequestOptions]
    }
  ]
})
export class AuthModule {}
