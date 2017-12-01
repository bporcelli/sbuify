import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PlayQueueComponent } from './play-queue.component';

import { AuthGuard } from '../auth/auth-guard.service';

const routes: Routes = [
    {
        path: 'play-queue',
        component: PlayQueueComponent,
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
export class PlayerRoutingModule {}
