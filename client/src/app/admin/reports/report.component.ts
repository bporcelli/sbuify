import { Component, OnInit } from '@angular/core';
import { ReportService } from "./report.service";

@Component({
  template: ''
})
export class ReportComponent implements OnInit {

  /** Is the report loading? */
  protected loading: boolean = true;

  /** Report data */
  protected data: any = null;

  constructor(protected reportService: ReportService) {}

  ngOnInit(): void {
    this.onSettingsChanged();
  }

  onSettingsChanged() {
    this.loading = true;
    this.getReportData();
  }

  getReportData() {
    // must be implemented by subclasses
  }

  protected handleNewData(data: any) {
    this.loading = false;
    this.data = data;
  }

  protected handleError(err: any) {
    this.loading = false;
    // todo: display error message
    console.log('error occurred while generating report:', err);
  }
}
