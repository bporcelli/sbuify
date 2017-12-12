import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BrowseComponent } from './browse.component';
import { OverviewComponent } from './overview.component';
import { GenresComponent } from './genres/genres.component';
import { NewReleasesComponent } from './new-releases.component';
import { ConcertsComponent } from '../concerts/concerts.component';
import { AuthGuard } from '../auth/auth-guard.service';
import { GenreResolver } from "./genres/genre-resolver.service";
import { GenreComponent } from "./genres/genre.component";
import { GenreDetailResolver } from "./genres/genre-detail-resolver.service";
import { UserLocationResolver } from "../user/user-location-resolver.service";

const browseRoutes: Routes = [
  {
    path: 'browse',
    component: BrowseComponent,
    canActivate: [ AuthGuard ],
    canActivateChild: [ AuthGuard ],
    children: [
      {
        path: 'overview',
        component: OverviewComponent
      },
      {
        path: 'genres',
        component: GenresComponent,
        resolve: {
          genres: GenreResolver
        }
      },
      {
        path: 'genres/:id',
        component: GenreComponent,
        resolve: {
          genre: GenreDetailResolver
        }
      },
      {
        path: 'new-releases',
        component: NewReleasesComponent
      },
      {
        path: 'concerts',
        component: ConcertsComponent,
        resolve: {
          location: UserLocationResolver
        }
      },
      {
        path: '',
        redirectTo: 'overview',
        pathMatch: 'full'
      }
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
