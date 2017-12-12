import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminHomeComponent } from "./admin-home.component";
import { AdminAuthGuard } from "../auth/admin-auth-guard.service";
import { UserListComponent } from "./user-list.component";
import { CreateUserComponent } from "./create-user.component";
import { RoyaltiesComponent } from "./royalties.component";
import { ReportsComponent } from "./reports.component";
import { SubscribersReportComponent } from "./reports/subscribers-report.component";
import { TrendsReportComponent } from "./reports/trends-report.component";
import { SongReportComponent } from "./reports/song-report.component";
import { ArtistReportComponent } from "./reports/artist-report.component";

const routes: Routes = [
  {
    path: 'admin',
    canActivate: [ AdminAuthGuard ],
    canActivateChild: [ AdminAuthGuard ],
    children: [
      {
        path: 'home',
        component: AdminHomeComponent,
      },
      {
        path: 'users',
        component: UserListComponent
      },
      {
        path: 'create-user',
        component: CreateUserComponent
      },
      {
        path: 'royalties',
        component: RoyaltiesComponent
      },
      {
        path: 'reports',
        component: ReportsComponent,
        children: [
          {
            path: 'subscribers',
            component: SubscribersReportComponent
          },
          {
            path: 'trends',
            component: TrendsReportComponent
          },
          {
            path: 'song',
            component: SongReportComponent
          },
          {
            path: 'artist',
            component: ArtistReportComponent
          },
          {
            path: '',
            redirectTo: 'subscribers',
            pathMatch: 'full'
          }
        ]
      },
      {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AdminRoutingModule {}
