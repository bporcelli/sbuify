import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule as ACommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { CommonRoutingModule } from './common-routing.module';

import { NavbarComponent } from './navbar.component';
import { LeftSidebarComponent } from './left-sidebar.component';
import { UpgradeAccountComponent } from './upgrade-account.component';
import { CreatePlaylistComponent } from './create-playlist.component';
import { FormatDurationPipe } from "./format-duration.pipe";
import { FilterBoxComponent } from "./filter-box.component";

@NgModule({
  declarations: [
    NavbarComponent,
    LeftSidebarComponent,
    UpgradeAccountComponent,
    CreatePlaylistComponent,
    FormatDurationPipe,
    FilterBoxComponent
  ],
  imports: [
    CommonRoutingModule,
    BrowserModule,
    NgbModule,
    ACommonModule,
    FormsModule
  ],
  exports: [
    NavbarComponent,
    LeftSidebarComponent,
    FormatDurationPipe,
    FilterBoxComponent
  ]
})
export class CommonModule {}
