import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from "../auth/auth-guard.service";
import { SongsComponent } from "./songs.component";
import { AlbumsComponent } from "./albums.component";
import { ArtistsComponent } from "./artists.component";
import { RecentlyPlayedComponent } from "./recently-played.component";

const routes: Routes = [
  {
    path: 'songs',
    component: SongsComponent,
    canActivate: [ AuthGuard ]
  },
  {
    path: 'albums',
    component: AlbumsComponent,
    canActivate: [ AuthGuard ]
  },
  {
    path: 'artists',
    component: ArtistsComponent,
    canActivate: [ AuthGuard ]
  },
  {
    path: 'recently-played',
    component: RecentlyPlayedComponent,
    canActivate: [ AuthGuard ]
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
export class LibraryRoutingModule {}
