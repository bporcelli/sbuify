import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { ContextMenuModule } from 'ngx-contextmenu';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Ng2AutoCompleteModule } from 'ng2-auto-complete';
import { AdminRoutingModule } from "./admin-routing.module";
import { AdminHomeComponent } from "./admin-home.component";
import { AuthModule } from "../auth/auth.module";
import { UserListComponent } from "./user-list.component";
import { SharedModule } from "../shared/shared.module";
import { EditUserModalComponent } from "./edit-user-modal.component";
import { CreateUserComponent } from "./create-user.component";
import { RoyaltiesComponent } from "app/admin/royalties.component";
import { RoyaltiesService } from "./royalties.service";
import { ToStatusStringPipe } from "./to-status-string.pipe";
import { RoyaltyModalComponent } from "./royalty-modal.component";
import { ReportsComponent } from "./reports.component";
import { SubscribersReportComponent } from "./reports/subscribers-report.component";
import { SongReportComponent } from "./reports/song-report.component";
import { ArtistReportComponent } from "./reports/artist-report.component";
import { TrendsReportComponent } from "./reports/trends-report.component";
import { ReportService } from "./reports/report.service";
import { ReportComponent } from "./reports/report.component";

@NgModule({
  imports: [
    CommonModule,
    AdminRoutingModule,
    AuthModule,
    SharedModule,
    InfiniteScrollModule,
    ContextMenuModule,
    NgbModule,
    FormsModule,
    Ng2AutoCompleteModule
  ],
  declarations: [
    AdminHomeComponent,
    UserListComponent,
    EditUserModalComponent,
    CreateUserComponent,
    RoyaltiesComponent,
    ToStatusStringPipe,
    RoyaltyModalComponent,
    ReportsComponent,
    ReportComponent,
    SubscribersReportComponent,
    TrendsReportComponent,
    SongReportComponent,
    ArtistReportComponent
  ],
  entryComponents: [
    EditUserModalComponent,
    RoyaltyModalComponent
  ],
  providers: [
    RoyaltiesService,
    ReportService
  ]
})
export class AdminModule {}
