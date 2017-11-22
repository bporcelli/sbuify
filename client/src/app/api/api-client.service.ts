import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { URLSearchParams, QueryEncoder } from '@angular/http';

class MyQueryEncoder extends QueryEncoder {

  /**
   * Override of QueryEncoder.encodeValue that encodes the character '+'.
   */
  encodeValue(v: string): string {
    return super.encodeValue(v).replace('+', '%2B');
  }
}

@Injectable()
export class APIClient extends HttpClient {

  /**
   * Make a POST request with content type application/x-www-form-urlencoded.
   *
   * @param uri Request URI.
   * @param data Form data.
   * @return Observable that sends the POST request when subscribed.
   */
  public postAsForm(uri: string, data: object) {
    let headers = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/x-www-form-urlencoded');

    let body = new URLSearchParams('', new MyQueryEncoder());
    for (let key in data) {
      body.append(key, data[key]);
    }

    return this.post(uri, body.toString(), {
      observe: 'response',
      headers: headers,
      responseType: 'text'  // required to consider empty responses successful
    });
  }
}
