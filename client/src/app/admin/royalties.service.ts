import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { APIClient } from "../shared/api-client.service";
import { Observable } from 'rxjs/Rx';

@Injectable()
export class RoyaltiesService {

  constructor(private apiClient: APIClient) {}

  /** Get all payment periods */
  getPaymentPeriods(): Observable<any[]> {
    return this.apiClient.get<any[]>('/api/royalty-payments/periods');
  }

  /** Get a page of royalty payments filtered by status and period */
  getRoyaltyPayments(page: number, status: string, period: string): Observable<any[]> {
    let params = new HttpParams();

    params = params.set("page", page.toString());

    if (status != 'ALL') {
      params = params.set("status", status);
    }
    if (period) {
      params = params.set("period", period);
    }

    return this.apiClient.get<any[]>('/api/royalty-payments', { params: params });
  }

  /** Mark a payment as paid */
  markPaid(payment: any): Observable<boolean> {
    return this.apiClient.post('/api/royalty-payments/' + payment['id'] + '/pay', null).mapTo(true);
  }
}
