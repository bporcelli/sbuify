import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MomentModule } from 'angular2-moment';

import { PlaylistDetailComponent } from './playlist-detail.component';
import { PlaylistDetailResolver } from './playlist-resolver.service';

import { AuthGuard } from '../auth/auth-guard.service';

const playlistRoutes: Routes = [
    {
        path: 'playlist/:id',
        component: PlaylistDetailComponent,
        canActivate: [ AuthGuard ],
        resolve: {
            playlist: PlaylistDetailResolver
        }
    },
];

@NgModule({
    declarations: [
        PlaylistDetailComponent
    ],
    imports: [
        RouterModule.forChild(playlistRoutes),
        CommonModule,
        NgbModule,
        MomentModule
    ],
    providers: [
        PlaylistDetailResolver
    ]
})
export class PlaylistDetailModule {}
