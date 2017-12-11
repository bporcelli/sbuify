import { Injectable } from '@angular/core';
import { APIClient } from "../shared/api-client.service";
import { BehaviorSubject } from "rxjs/Rx";
import { Observable } from "rxjs/Rx";

@Injectable()
export class PreferencesService {
  private preferencesSubject: BehaviorSubject<object> = new BehaviorSubject({});

  constructor(private client: APIClient) {
    // initialize the preferences object
    this.client.get<object>("/api/customer/preferences")
      .subscribe(
        (preferences: object) => this.preferencesSubject.next(preferences),
        (err: any) => this.handleError(err, null)
      );
  }

  setPreference(key: string, value: any): Observable<boolean> {
    return this.client.put('/api/customer/preferences/' + key, value)
      .catch(this.handleError)
      .map(() => {
        let preferences = this.preferencesSubject.value || {};
        preferences[key] = value;
        this.preferencesSubject.next(preferences);
        return true;
      });
  }

  setPreferences(preferences: object): Observable<boolean> {
    return this.client.put('/api/customer/preferences', preferences)
      .catch(this.handleError)
      .map(() => {
        let updated = this.preferencesSubject.value;
        for (let key in preferences) {
          updated[key] = preferences[key];
        }
        this.preferencesSubject.next(updated);
        return true;
      });
  }

  get preferences(): Observable<object> {
    return this.preferencesSubject.asObservable().distinctUntilChanged();
  }

  private handleError(err: any, caught: Observable<any>): Observable<any> {
    // todo: show error message
    console.log('error occurred while getting or setting preferences:', err);
    return Observable.throw(err);
  }
}
