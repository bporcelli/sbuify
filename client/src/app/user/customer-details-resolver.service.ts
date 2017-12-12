import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Customer } from "./customer";
import { UserService } from "./user.service";

@Injectable()
export class CustomerDetailsResolver implements Resolve<Customer> {
  constructor(
    private service: UserService,
    private router: Router
  ) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Customer> {
    let id = route.paramMap.get('id');

    return this.service.getUser(id).take(1).map(customer => {
      if (customer && customer.type == 'customer') {
        return <Customer>customer;
      } else { // id not found
        // todo: show error
        this.router.navigate(['/browse/overview']);
        return null;
      }
    });
  }
}
