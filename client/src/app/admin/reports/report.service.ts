import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { APIClient } from "../../shared/api-client.service";

@Injectable()
export class ReportService {

  constructor(private apiClient: APIClient) {}

  getSubscribersReport(window: number): Observable<any> {
    return this.apiClient.get('/api/reports/subscribers', { params: this.getParams(window) });
  }

  getTrendsReport(window: number): Observable<any> {
    return this.apiClient.get('/api/reports/trends', { params: this.getParams(window) });
  }

  getSongReport(songId: number, window: number): Observable<any> {
    return this.apiClient.get('/api/reports/song/' + songId, { params: this.getParams(window) });
  }

  getArtistReport(artistId: number, window: number): Observable<any> {
    return this.apiClient.get('/api/reports/artist/' + artistId, { params: this.getParams(window) });
  }

  private getParams(window: number): HttpParams {
    let params = new HttpParams();
    return params.set("window", window.toString());
  }
}
