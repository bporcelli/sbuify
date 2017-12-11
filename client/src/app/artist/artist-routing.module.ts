import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArtistDetailComponent } from './artist-detail.component';
import { OverviewComponent } from './overview.component';
import { RelatedArtistsComponent } from './related-artists.component';
import { AboutComponent } from './about.component';
import { AuthGuard } from '../auth/auth-guard.service';
import { ArtistDetailResolver } from "./artist-resolver.service";
import { ArtistConcertsComponent } from "./artist-concerts.component";

const routes: Routes = [
  {
    path: 'artist/:id',
    component: ArtistDetailComponent,
    canActivate: [ AuthGuard ],
    canActivateChild: [ AuthGuard ],
    resolve: {
      artist: ArtistDetailResolver
    },
    children: [
      {
        path: 'overview',
        component: OverviewComponent
      },
      {
        path: 'related',
        component: RelatedArtistsComponent
      },
      {
        path: 'about',
        component: AboutComponent
      },
      {
        path: 'concerts',
        component: ArtistConcertsComponent
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
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class ArtistRoutingModule {}
