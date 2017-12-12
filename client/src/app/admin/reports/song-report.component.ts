import { Component } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { ReportComponent } from "./report.component";
import { APIClient } from "../../shared/api-client.service";
import { Config } from "../../config";
import { ReportService } from "./report.service";
import { Song } from "../../songs/song";
import { BehaviorSubject } from "rxjs/Rx";

@Component({
  templateUrl: './song-report.component.html'
})
export class SongReportComponent extends ReportComponent {

  /** Song list */
  private songsSubject: BehaviorSubject<Song[]> = new BehaviorSubject([]);

  /** Reporting window */
  private days: number = 7;

  /** Selected song */
  private songId: number = - 1;

  constructor(
    protected apiClient: APIClient,
    protected reportService: ReportService
  ) {
    super(reportService);
  }

  getReportData() {
    if (this.songId == -1 || this.songId == null) {
      this.loading = false;
      return;
    }
    this.reportService.getSongReport(this.songId, this.days)
      .subscribe(
        (data) => this.handleNewData(data),
        (err: any) => this.handleError(err)
      );
  }

  getSongResults() {
    return this.songsSubject;
  }

  private getSongs(value: string): void {
    let params: HttpParams = new HttpParams();

    params = params.set('query', value);
    params = params.set('type', 'song');
    params = params.set('limit', Config.ITEMS_PER_PAGE.toString());
    params = params.set('offset', '0');

    this.apiClient.get<any[]>("/api/search", { params: params })
      .subscribe((results) => this.songsSubject.next(results));
  }

  set song(song: any) {
    this.songId = song.id;
  }
}
