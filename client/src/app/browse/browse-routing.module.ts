import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { BrowseComponent } from './browse.component';
import { OverviewComponent } from './overview.component';
import { ChartsComponent } from './charts.component';
import { GenresComponent } from './genres.component';
import { NewReleasesComponent } from './new-releases.component';
import { ConcertsComponent } from '../concerts/concerts.component';

import { AuthGuard } from '../auth/auth-guard.service';

const browseRoutes: Routes = [
    {
        path: 'browse',
        component: BrowseComponent,
        canActivate: [AuthGuard],
        canActivateChild: [AuthGuard],
        children: [
            { path: 'overview', component: OverviewComponent },
            { path: 'charts', component: ChartsComponent },
            { path: 'genres', component: GenresComponent },
            { path: 'new-releases', component: NewReleasesComponent },
            { path: 'concerts', component: ConcertsComponent },
            { path: '', redirectTo: 'overview', pathMatch: 'full' }
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(browseRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class BrowseRoutingModule {}
