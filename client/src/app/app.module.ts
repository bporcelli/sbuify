import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ContextMenuModule } from 'ngx-contextmenu';
import { FormsModule } from '@angular/forms';
import { TruncateModule } from "ng2-truncate";
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { SharedModule } from './shared/shared.module';
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
import { GenreService } from "./browse/genres/genre.service";
import { AlbumModule } from "./album/album.module";
import { PlayerService } from "./playback/player.service";
import { PlayQueueService } from "./play-queue/play-queue.service";
import { LibraryService } from "./library/library.service";
import { LibraryModule } from "./library/library.module";
import { StreamService } from "./playback/stream.service";
import { PlayQueueModule } from "./play-queue/play-queue.module";
import { LeftSidebarComponent } from "./left-sidebar.component";
import { NavbarComponent } from "./navbar.component";
import { PlaybarComponent } from "./playbar.component";
import { LyricsToHtmlPipe } from "./lyrics-to-html.pipe";
import { LyricsModalComponent } from "./lyrics-modal.component";
import { APIClient } from "./shared/api-client.service";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LeftSidebarComponent,
    PlaybarComponent,
    LyricsModalComponent,
    LyricsToHtmlPipe
  ],
  entryComponents: [
    LyricsModalComponent
  ],
  imports: [
    NgbModule.forRoot(),
    BrowserModule,
    SharedModule,
    HttpClientModule,
    FormsModule,
    TruncateModule,
    AuthModule,
    BrowseModule,
    ArtistModule,
    PlaylistModule,
    SongsModule,
    AlbumModule,
    LibraryModule,
    SearchModule,
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
    APIClient,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {}
