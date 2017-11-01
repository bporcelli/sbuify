import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';

import { ArtistDetailComponent } from './artist-detail.component';
import { OverviewComponent } from './overview.component';
import { RelatedArtistsComponent } from './related-artists.component';
import { AboutComponent } from './about.component';
import { ConcertsComponent } from '../concerts/concerts.component';

import { AuthGuard } from '../auth-guard.service';

const artistRoutes: Routes = [
    { 
        path: 'artist/:id',
        component: ArtistDetailComponent,
        canActivate: [ AuthGuard ],
        canActivateChild: [ AuthGuard ],
        children: [
            { path: 'overview', component: OverviewComponent },
            { path: 'related', component: RelatedArtistsComponent },
            { path: 'about', component: AboutComponent },
            { path: 'concerts', component: ConcertsComponent },
            { path: '', redirectTo: 'overview', pathMatch: 'full' }
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(artistRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class ArtistDetailRoutingModule {}
