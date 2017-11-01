import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SongsComponent } from './songs.component';

import { AuthGuard } from '../auth-guard.service';

const songsRoutes: Routes = [
    { 
        path: 'songs',
        component: SongsComponent,
        canActivate: [ AuthGuard ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(songsRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class SongsRoutingModule { }
