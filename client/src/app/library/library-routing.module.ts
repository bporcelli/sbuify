import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from "../auth/auth-guard.service";
import { SongsComponent } from "./songs.component";
import { AlbumsComponent } from "./albums.component";

const routes: Routes = [
  {
    path: 'songs',
    component: SongsComponent,
    canActivate: [ AuthGuard ]
  },
  {
    path: 'albums',
    component: AlbumsComponent
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
