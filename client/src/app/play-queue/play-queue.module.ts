import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { PlayQueueComponent } from "./play-queue.component";
import { AuthGuard } from "../auth/auth-guard.service";
import { SongsModule } from "../songs/songs.module";

const routes: Routes = [
  {
    path: 'play-queue',
    component: PlayQueueComponent,
    canActivate: [ AuthGuard ]
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SongsModule
  ],
  declarations: [
    PlayQueueComponent
  ],
  exports: [
    RouterModule
  ]
})
export class PlayQueueModule {}
