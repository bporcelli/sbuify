import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule as ACommonModule } from '@angular/common';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { CommonRoutingModule } from './common-routing.module';

import { NavbarComponent } from './navbar.component';
import { PlaybarComponent } from './playbar.component';
import { LeftSidebarComponent } from './left-sidebar.component';
import { UpgradeAccountComponent } from './upgrade-account.component';
import { CreatePlaylistComponent } from './create-playlist.component';

@NgModule({
    declarations: [
        NavbarComponent,
        PlaybarComponent,
        LeftSidebarComponent,
        UpgradeAccountComponent,
        CreatePlaylistComponent,
    ],
    imports: [
        CommonRoutingModule,
        BrowserModule,
        NgbModule,
        ACommonModule
    ],
    exports: [
        NavbarComponent,
        PlaybarComponent,
        LeftSidebarComponent,
    ]
})
export class CommonModule {}
