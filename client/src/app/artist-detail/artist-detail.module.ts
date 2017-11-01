import { NgModule } from '@angular/core';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ArtistDetailComponent } from './artist-detail.component';
import { ArtistDetailRoutingModule } from './artist-detail-routing.module';

import { OverviewComponent } from './overview.component';
import { RelatedArtistsComponent } from './related-artists.component';
import { AboutComponent } from './about.component';

@NgModule({
    imports: [
        NgbModule,
        ArtistDetailRoutingModule
    ],
    declarations: [
        ArtistDetailComponent,
        OverviewComponent,
        RelatedArtistsComponent,
        AboutComponent
    ],
})
export class ArtistDetailModule {}
