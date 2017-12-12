import { Component } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { ReportComponent } from "./report.component";
import { APIClient } from "../../shared/api-client.service";
import { Config } from "../../config";
import { ReportService } from "./report.service";
import { BehaviorSubject } from "rxjs/Rx";
import { Artist } from "../../artist/artist";

@Component({
  templateUrl: './artist-report.component.html'
})
export class ArtistReportComponent extends ReportComponent {

  /** Artist list */
  private artistSubject: BehaviorSubject<Artist[]> = new BehaviorSubject([]);

  /** Reporting window */
  private days: number = 7;

  /** Selected song */
  private artistId: number = - 1;

  constructor(
    protected apiClient: APIClient,
    protected reportService: ReportService
  ) {
    super(reportService);
  }

  getReportData() {
    if (this.artistId == -1 || this.artistId == null) {
      this.loading = false;
      return;
    }
    this.reportService.getArtistReport(this.artistId, this.days)
      .subscribe(
        (data) => this.handleNewData(data),
        (err: any) => this.handleError(err)
      );
  }

  getArtistResults() {
    return this.artistSubject;
  }

  private getArtists(value: string): void {
    let params: HttpParams = new HttpParams();

    params = params.set('query', value);
    params = params.set('type', 'artist');
    params = params.set('limit', Config.ITEMS_PER_PAGE.toString());
    params = params.set('offset', '0');

    this.apiClient.get<any[]>("/api/search", { params: params })
      .subscribe((results) => this.artistSubject.next(results));
  }

  set artist(artist: any) {
    this.artistId = artist.id;
  }
}
