import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { APIClient } from "../shared/api-client.service";
import { Observable } from 'rxjs/Rx';

@Injectable()
export class FollowingService {

  /** Cache of followed entities */
  private followed: object = {};

  constructor(private client: APIClient) {}

  /** Follow an entity */
  follow(type: string, id: any): Observable<boolean> {
    let key = this.getKey(type, id);
    let params = this.getRequestParams(type, id);

    return this.client.put('/api/customer/following', null, { params: params })
      .map(() => {
        this.followed[key] = true;
        return true;
      });
  }

  /** Unfollow an entity */
  unfollow(type: string, id: any): Observable<boolean> {
    let key = this.getKey(type, id);
    let params = this.getRequestParams(type, id);

    return this.client.delete('/api/customer/following', { params: params })
      .map(() => {
        this.followed[key] = false;
        return false;
      });
  }

  private getKey(type: string, id: any): string {
    return type + '-' + id;
  }

  private getRequestParams(type: string, id: any): HttpParams {
    let params = new HttpParams();

    params = params.set('type', type);
    params = params.set('id', id);

    return params;
  }

  private handleError(err: any): void {
    // todo: show error
    console.log('an error occurred while following or unfollowing an entity:', err);
  }
}
