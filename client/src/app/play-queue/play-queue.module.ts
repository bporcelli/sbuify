import { NgModule } from '@angular/core';

import { PlayQueueComponent } from './play-queue.component';
import { PlayQueueRoutingModule } from './play-queue-routing.module';

@NgModule({
    imports: [
        PlayQueueRoutingModule
    ],
    declarations: [
        PlayQueueComponent
    ]
})
export class PlayQueueModule {}
