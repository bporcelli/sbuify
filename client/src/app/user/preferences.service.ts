import { Injectable } from '@angular/core';
import { APIClient } from "../common/api-client.service";
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
        (err: any) => console.log('failed to get preferences from server. error was:', err)
      );
  }

  setPreference(key: string, value: any): void {
    this.client.put('/api/customer/preferences/' + key, value)
      .subscribe(
        () => {
          let preferences = this.preferencesSubject.value || {};
          preferences[key] = value;
          this.preferencesSubject.next(preferences);
        },
        (err: any) => console.log('failed to set preference ' + key + ':', err)
      );
  }

  setPreferences(preferences: object): void {
    this.client.put('/api/customer/preferences', preferences)
      .subscribe(
        (resp: any) => this.preferencesSubject.next(preferences),
        (err: any) => console.log('failed to update preferences:', err)
      );
  }

  get preferences(): Observable<object> {
    return this.preferencesSubject.asObservable().distinctUntilChanged();
  }
}
