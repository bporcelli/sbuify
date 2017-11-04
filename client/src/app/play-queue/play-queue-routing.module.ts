import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PlayQueueComponent } from './play-queue.component';

import { AuthGuard } from '../auth/auth-guard.service';

const playQueueRoutes: Routes = [
    {
        path: 'play-queue',
        component: PlayQueueComponent,
        canActivate: [ AuthGuard ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(playQueueRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class PlayQueueRoutingModule {}
