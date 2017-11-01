import { NgModule } from '@angular/core';

import { SongsComponent } from './songs.component';
import { SongsRoutingModule } from './songs-routing.module';

@NgModule({
    imports: [
        SongsRoutingModule
    ],
    declarations: [
        SongsComponent
    ]
})
export class SongsModule { }
