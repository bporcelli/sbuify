import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ContextMenuModule } from 'ngx-contextmenu';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { CommonModule } from './common/common.module';
import { BrowseModule } from './browse/browse.module';
import { ArtistModule } from './artist/artist.module';
import { PlaylistModule } from './playlist/playlist.module';
import { SongsModule } from './songs/songs.module';
import { SearchModule } from './search/search.module';
import { GuestModule } from './guest/guest.module';
import { ConcertsModule } from './concerts/concerts.module';
import { SettingsModule } from './settings/settings.module';
import { UserModule } from './user/user.module';
import { AuthModule } from "./auth/auth.module";

import { PlaylistService } from './playlist/playlist.service';
import { SearchService } from "./search/search.service";
import { AuthInterceptor } from "./auth/auth-interceptor.";
import { APIModule } from "./api/api.module";
import { GenreService } from "./browse/genres/genre.service";
import { AlbumModule } from "./album/album.module";
import { PlayerModule } from "./player/player.module";
import { PlayerService } from "./player/player.service";
import { PlayQueueService } from "./play-queue/play-queue.service";
import { LibraryService } from "./library/library.service";
import { LibraryModule } from "./library/library.module";
import { StreamService } from "./player/stream.service";
import { PlayQueueModule } from "./play-queue/play-queue.module";

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
    ArtistModule,
    PlaylistModule,
    SongsModule,
    AlbumModule,
    LibraryModule,
    SearchModule,
    PlayerModule,
    PlayQueueModule,
    GuestModule,
    ConcertsModule,
    SettingsModule,
    UserModule,
    AppRoutingModule,
    ContextMenuModule.forRoot({
      useBootstrap4: true
    })
  ],
  providers: [
    PlaylistService,
    SearchService,
    GenreService,
    PlayerService,
    PlayQueueService,
    LibraryService,
    StreamService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {}
