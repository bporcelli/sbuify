import { Component } from '@angular/core';
import { ReportComponent } from "./report.component";

@Component({
  templateUrl: './subscribers-report.component.html'
})
export class SubscribersReportComponent extends ReportComponent {

  /** Reporting window (days) */
  private days: number = 7;

  getReportData() {
    this.reportService.getSubscribersReport(this.days)
      .subscribe(
        (data) => this.handleNewData(data),
        (err: any) => this.handleError(err)
      );
  }
}
