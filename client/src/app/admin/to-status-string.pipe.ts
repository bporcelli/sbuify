import { Pipe, PipeTransform } from '@angular/core';

/**
 * A pipe to transform a PaymentStatus enum value into a "pretty" string.
 */
@Pipe({
  name: 'toStatusString'
})
export class ToStatusStringPipe implements PipeTransform {

  transform(value: any, ...args): any {
    switch (value) {
      case 'PAID':
        return 'Paid';
      case 'PENDING_PAYMENT':
        return 'Pending Payment';
      default:
        return '';
    }
  }
}
