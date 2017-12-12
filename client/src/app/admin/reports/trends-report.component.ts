import { Component } from '@angular/core';
import { ReportComponent } from "./report.component";

@Component({
  templateUrl: './trends-report.component.html'
})
export class TrendsReportComponent extends ReportComponent {

  /** Reporting window (days) */
  private days: number = 7;

  getReportData() {
    this.reportService.getTrendsReport(this.days)
      .subscribe(
        (data) => this.handleNewData(data),
        (err: any) => this.handleError(err)
      );
  }
}
