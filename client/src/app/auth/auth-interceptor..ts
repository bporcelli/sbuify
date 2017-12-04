import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from "rxjs/Rx";
import { tokenGetter } from "./helpers";

/**
 * Intercepts outgoing requests to attach the authenticated user's JWT.
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token: string = tokenGetter()();
    if (token != null) {
      let transformed: HttpRequest<any> = req.clone({
        headers: req.headers.set("Authorization", token)
      });
      return next.handle(transformed);
    }
    return next.handle(req);
  }
}
