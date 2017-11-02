import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RecaptchaModule } from 'ng-recaptcha';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { CommonModule } from './common/common.module';
import { BrowseModule } from './browse/browse.module';
import { ArtistDetailModule } from './artist-detail/artist-detail.module';
import { PlaylistDetailModule } from './playlist-detail/playlist-detail.module';
import { SongsModule } from './songs/songs.module';
import { PlayQueueModule } from './play-queue/play-queue.module';
import { SearchModule } from './search/search.module';
import { GuestModule } from './guest/guest.module';
import { ConcertsModule } from './concerts/concerts.module';
import { SettingsModule } from './settings/settings.module';

import { AuthService } from './auth.service';
import { PlaylistService } from './playlist.service';
import { AuthGuard } from './auth-guard.service';

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        NgbModule.forRoot(),
        RecaptchaModule.forRoot(),
        BrowserModule,
        CommonModule,
        HttpClientModule,
        BrowseModule,
        ArtistDetailModule,
        PlaylistDetailModule,
        SongsModule,
        PlayQueueModule,
        SearchModule,
        GuestModule,
        ConcertsModule,
        SettingsModule,
        AppRoutingModule
    ],
    providers: [
        AuthService,
        PlaylistService,
        AuthGuard
    ],
    bootstrap: [ AppComponent ]
})
export class AppModule {
    // TODO: Remove
    constructor(router: Router) {
        console.log('Routes: ', JSON.stringify(router.config, undefined, 2));
    }
}
