import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { CommonModule } from './common/common.module';
import { BrowseModule } from './browse/browse.module';
import { ArtistDetailModule } from './artist/artist-detail.module';
import { PlaylistDetailModule } from './playlist/playlist-detail.module';
import { SongsModule } from './songs/songs.module';
import { PlayQueueModule } from './play-queue/play-queue.module';
import { SearchModule } from './search/search.module';
import { GuestModule } from './guest/guest.module';
import { ConcertsModule } from './concerts/concerts.module';
import { SettingsModule } from './settings/settings.module';
import { UserModule } from './user/user.module';
import { AuthModule } from "./auth/auth.module";

import { PlaylistService } from './playlist/playlist.service';
import { SearchService } from "./search/search.service";
import { AuthInterceptor } from "./auth/auth-interceptor.";
import {APIModule} from "./api/api.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    NgbModule.forRoot(),
    BrowserModule,
    CommonModule,
    HttpClientModule,
    APIModule,
    AuthModule,
    BrowseModule,
    ArtistDetailModule,
    PlaylistDetailModule,
    SongsModule,
    PlayQueueModule,
    SearchModule,
    GuestModule,
    ConcertsModule,
    SettingsModule,
    UserModule,
    AppRoutingModule
  ],
  providers: [
    PlaylistService,
    SearchService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {}
