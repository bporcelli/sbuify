import { NgModule } from '@angular/core';

import { BrowseComponent } from './browse.component';
import { BrowseRoutingModule } from './browse-routing.module';

import { OverviewComponent } from './overview.component';
import { ChartsComponent } from './charts.component';
import { GenresComponent } from './genres.component';
import { NewReleasesComponent } from './new-releases.component';

@NgModule({
    imports: [
        BrowseRoutingModule
    ],
    declarations: [
        BrowseComponent,
        OverviewComponent,
        ChartsComponent,
        GenresComponent,
        NewReleasesComponent
    ]
})
export class BrowseModule {}